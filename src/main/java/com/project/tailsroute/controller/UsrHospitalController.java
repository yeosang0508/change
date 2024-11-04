package com.project.tailsroute.controller;

import com.opencsv.CSVReader;
import com.project.tailsroute.service.HospitalService;
import com.project.tailsroute.vo.Hospital;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class UsrHospitalController {

    // 루트 디렉토리에 있는 .env 파일에서 API 키를 불러옴.
    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;
    @Value("${GOOGLE_MAP_ID}")
    private String GOOGLE_MAP_ID;

    // 루트 디렉토리에 있는 .env 파일에서 API 키 불러옴
    @Value("${NAVER_MAP_CLIENT_ID}")
    private String naver_clientId; // 네이버 클라이언트 ID
    @Value("${NAVER_MAP_CLIENT_SECRET}")
    private String naver_clientSecret;  // 네이버 클라이언트 Secret

    @Autowired
    private HospitalService hospitalService;

    ////
    private final Rq rq;

    public UsrHospitalController(Rq rq) {
        this.rq = rq;
    }
    ////

    @GetMapping("/usr/hospital/main")
    public String showMain(Model model) {

        ////
        boolean isLogined = rq.isLogined();
        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }
        model.addAttribute("isLogined", isLogined);
        ////

        model.addAttribute("GOOGLE_MAP_API_KEY", API_KEY);
        model.addAttribute("GOOGLE_MAP_ID", GOOGLE_MAP_ID);

        return "usr/map/hospital";
    }

    @GetMapping("/hospitals")
    @ResponseBody
    public List<Hospital> getAllHospitals() {
        // DB에서 모든 병원 데이터를 가져와 반환
        return hospitalService.getAllHospitals();
    }

    // DB 데이터 조회하는 테스트 코드
    @RequestMapping("/usr/hospital/hospitalList")
    public String showHospitals(Model model) {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        model.addAttribute("hospitals", hospitals);
        return "usr/hospital/hospitals";
    }

    // 주소 클린징 함수 추가
    private String cleanAddress(String address) {
        return address.replaceAll("\\(.*\\)", "").trim();  // 괄호 안의 내용을 제거하고 앞뒤 공백을 제거
    }

    @GetMapping("/usr/hospital/updateCoordinates")
    @ResponseBody
    public String updateHospitalCoordinates_Naver() {
        List<Hospital> hospitals = hospitalService.getHospitalsWithoutCoordinates();

        int count = 0;

        for (Hospital hospital : hospitals) {
            String address = null;

            // 도로명 주소가 빈 문자열이 아닌지 확인
            if (hospital.getRoadAddress() != null && !hospital.getRoadAddress().trim().isEmpty()) {
                address = hospital.getRoadAddress();
            }
            // 지번 주소가 빈 문자열이 아닌지 확인
            else if (hospital.getJibunAddress() != null && !hospital.getJibunAddress().trim().isEmpty()) {
                address = hospital.getJibunAddress();
            } else {
                // System.out.println("주소 정보가 없는 병원 ID: " + hospital.getId());
                continue; // 주소가 없으면 다음 병원으로 넘어감
            }

            // 좌표를 가져와서 업데이트
            String coordinates = getCoordinatesFromAddress_NAVER(address);
            if (coordinates != null) {
                String[] latLng = coordinates.split(",");
                hospitalService.updateHospitalCoordinates(hospital.getId(), latLng[0], latLng[1]);
                // System.out.println("좌표 업데이트 성공: 병원 ID " + hospital.getId());
            } else {
                // System.out.println("좌표를 가져오지 못한 병원 ID: " + hospital.getId());
            }

            count++;

//            if (count > 10)
//                break;
        }

        return "Coordinates updated for hospitals without latitude/longitude " + count;
    }

//    @GetMapping("/usr/hospital/updateCoordinatesGoogle")
//    @ResponseBody
//    public String updateHospitalCoordinates() {
//        List<Hospital> hospitals = hospitalService.getHospitalsWithoutCoordinates();
//        String apiKey = API_KEY;
//
//        int count = 0;
//
//        for (Hospital hospital : hospitals) {
//            String address = null;
//
//            // 도로명 주소가 빈 문자열이 아닌지 확인
//            if (hospital.getRoadAddress() != null && !hospital.getRoadAddress().trim().isEmpty()) {
//                address = hospital.getRoadAddress();
//            }
//            // 지번 주소가 빈 문자열이 아닌지 확인
//            else if (hospital.getJibunAddress() != null && !hospital.getJibunAddress().trim().isEmpty()) {
//                address = hospital.getJibunAddress();
//            } else {
//                System.out.println("주소 정보가 없는 병원 ID: " + hospital.getId());
//                continue; // 주소가 없으면 다음 병원으로 넘어감
//            }
//
//            // 좌표를 가져와서 업데이트
//            String coordinates = getCoordinatesFromAddress(address, apiKey);
//            if (coordinates != null) {
//                String[] latLng = coordinates.split(",");
//                hospitalService.updateHospitalCoordinates(hospital.getId(), latLng[0], latLng[1]);
//                System.out.println("좌표 업데이트 성공: 병원 ID " + hospital.getId());
//            } else {
//                System.out.println("좌표를 가져오지 못한 병원 ID: " + hospital.getId());
//            }
//
//            count++;
//
//            if (count > 3)
//                break;
//        }
//
//        return "Coordinates updated for hospitals without latitude/longitude";
//    }

    private String getCoordinatesFromAddress_NAVER(String address) {

        // 클린징을 하지 않고 원본 주소를 그대로 사용
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;

        // System.out.println("요청하는 주소: <" + address + ">");
        // System.out.println("API 요청 URL: " + apiUrl);

        // 헤더에 클라이언트 ID와 시크릿 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naver_clientId);
        headers.set("X-NCP-APIGW-API-KEY", naver_clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate로 API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 파싱
                String responseBody = response.getBody();
                JSONObject json = new JSONObject(responseBody);  // JSONObject 생성 시 예외 처리 필요

                if (json.has("addresses") && json.getJSONArray("addresses").length() > 0) {
                    JSONObject location = json.getJSONArray("addresses").getJSONObject(0);
                    String lat = location.getString("y");  // 위도
                    String lng = location.getString("x");  // 경도
                    return lat + "," + lng;
                } else {
                    System.out.println("No results found.");
                    return null;
                }
            } else {
                System.out.println("API 호출 실패: " + response.getStatusCode());
                return null;
            }
        } catch (JSONException e) {
            System.out.println("JSON 파싱 오류: " + e.getMessage());
            e.printStackTrace();  // 오류 스택 트레이스 출력
            return null;
        }
    }

    private String getCoordinatesFromAddressGoogle(String address, String apiKey) {
        String coordinates = null;
        try {
            // 클린징을 하지 않고 원본 주소를 그대로 사용
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") + "&key=" + apiKey;

            System.out.println("요청하는 주소: <" + address + ">");
            System.out.println("API 요청 URL: " + apiUrl);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            JSONObject json = new JSONObject(response);

            System.out.println("API 응답 내용: " + json.toString());

            if ("OK".equals(json.getString("status"))) {
                JSONObject location = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                String lat = location.getDouble("lat") + "";
                String lng = location.getDouble("lng") + "";
                coordinates = lat + "," + lng;
            } else {
                System.out.println("Geocoding API 오류: " + json.getString("status") + " - " + json.optString("error_message"));
            }
        } catch (Exception e) {
            System.out.println("주소 변환 실패: " + address);
            e.printStackTrace();
        }
        return coordinates;
    }

    // 테스트용 코드
    @RequestMapping("/usr/hospital/showCSVData")
    @ResponseBody
    public String showCSVData() {

        StringBuilder output = new StringBuilder(); // CSV 파일 내용을 담을 StringBuilder

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("csv/data_veterinary_care.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"))) {

            if (inputStream == null) {
                throw new FileNotFoundException("CSV file not found in resources folder");
            }

            String[] nextLine;

            // CSV 파일에서 한 줄씩 읽어와 StringBuilder에 추가
            while ((nextLine = reader.readNext()) != null) {
                for (String token : nextLine) {
                    // HTML 인젝션 방지를 위해 이스케이프 처리
                    output.append(escapeHtml(token)).append(" "); // 각 CSV 값을 공백으로 구분하여 추가
                }
                output.append("<br>"); // 줄 바꿈 추가 (HTML)
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }

        return "showCSVData 성공! <hr>" + output.toString(); // CSV 파일 내용 출력
    }

    // HTML 인젝션 방지를 위한 메소드
    private String escapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // 일회성 코드(DB에 .csv 파일의 내용 넣기)
    @RequestMapping("/usr/hospital/importCsvToDatabase")
    @ResponseBody
    public String importCsvToDatabase() {

        StringBuilder output = new StringBuilder(); // 로그 출력용 StringBuilder
        CSVReader reader = null;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("csv/fulldata_veterinary_care.csv");

            if (inputStream == null) {
                throw new FileNotFoundException("CSV file not found in resources folder");
            }

            reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"));
            String[] nextLine;

            // CSV 파일에서 한 줄씩 읽어오기
            while ((nextLine = reader.readNext()) != null) {

                // 번호,소재지전화,소재지전체주소,도로명전체주소,사업장명 순으로 저장됨. 0~4

                String id = checkEmpty(nextLine[0]); // 번호
                String state = checkEmpty(nextLine[1]); // 영업 상태
                String callNumber = checkEmpty(nextLine[2]); // 소재지 전화
                String addressLocation = checkEmpty(nextLine[3]); // 소재지 전체 주소
                String addressStreet = checkEmpty(nextLine[4]); // 도로명 전체 주소
                String name = checkEmpty(nextLine[5]); // 사업장 명

                System.err.println(id);
                // int temp = Integer.parseInt(id);

                hospitalService.doInsertHospitalInfo(callNumber, addressLocation, addressStreet, name);

                output.append("Inserted: ").append(id).append(", ").append(callNumber).append(", ").append(addressLocation)
                        .append(", ").append(addressStreet).append(", ").append(name).append("<hr>");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // 리소스 해제
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "CSV to DB Insert 성공! <hr>" + output.toString();
    }

    // 빈 문자열을 null로 변환하는 함수
    private String checkEmpty(String value) {
        return (value != null && value.trim().isEmpty()) ? null : value;
    }

}