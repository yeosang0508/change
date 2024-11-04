package com.project.tailsroute.service;

import com.fazecast.jSerialComm.SerialPort;
import com.project.tailsroute.repository.GpsAlertRepository;
import com.project.tailsroute.vo.GpsAlert;

import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.NurigoApp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class GpsAlertService {

    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;

    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // 쓰레드 풀 생성
    private List<Future<?>> runningTasks; // 실행 중인 쓰레드 작업 관리

    // Twilio 계정의 SID와 인증 토큰을 저장
    @Value("${apiKey}")
    private String apiKey; // Twilio 계정 SID

    @Value("${apiSecret}")
    private String apiSecret; // Twilio 인증 토큰

    private static final double EARTH_RADIUS = 6371.0; // 지구 반지름 (km 단위)

    // 각 dogId에 대한 작업을 관리하는 Map
    private Map<Integer, Future<?>> gpsAlertTasks = new ConcurrentHashMap<>();

    @Autowired
    GpsAlertRepository gpsAlertRepository;

    public void startGpsDataListener() {
        List<GpsAlert> GpsAlerts = All();

        System.err.println("GPS 갯수 : " + GpsAlerts.size());

        /*
        String comPortName = determinedLocation.getExtra__comPortName(); // GPS 기기 연결 포트
        double lat2 = determinedLocation.getLatitude(); // 설정한 위도
        double lon2 = determinedLocation.getLongitude(); // 설정한 경도
        String dogName = determinedLocation.getExtra__dogName(); // 강아지 이름
        boolean chack = determinedLocation.getChack(); // 범위 벗어났는지 여부
        String userCellphoneNum = determinedLocation.getExtra__cellphoneNum(); // 주인 핸드폰번호
        */

        // GPS 데이터를 읽고 처리하는 메서드 호출
        for (GpsAlert gpsAlert : GpsAlerts) {
            // 각 GpsAlert에 대해 ExecutorService를 통해 작업 실행
            Future<?> task = executorService.submit(() -> readGpsData(gpsAlert));
            gpsAlertTasks.put(gpsAlert.getDogId(), task); // 작업을 맵에 저장
        }
    }

    // 특정 dogId에 대한 작업을 중지하고 다시 시작하는 메서드
    public synchronized void restartGpsDataListenerForDog(int dogId) {
        // 해당 dogId의 기존 작업이 있으면 중지
        Future<?> task = gpsAlertTasks.get(dogId);
        if (task != null) {
            task.cancel(true); // 해당 작업 중지
            gpsAlertTasks.remove(dogId);
        }

        // 새로운 작업 시작
        GpsAlert gpsAlert = getGpsAlert(dogId);
        Future<?> newTask = executorService.submit(() -> readGpsData(gpsAlert));
        gpsAlertTasks.put(dogId, newTask); // 새로운 작업을 Map에 저장
    }

    // 모든 GPS 작업을 중지하고 다시 시작하는 메서드
    public synchronized void restartGpsDataListener() {
        // 기존 모든 작업 중지
        for (Future<?> task : gpsAlertTasks.values()) {
            task.cancel(true); // 기존 작업 중지
        }
        gpsAlertTasks.clear(); // Map 초기화

        // 모든 GPS 데이터를 새로 시작
        List<GpsAlert> gpsAlerts = All();
        for (GpsAlert gpsAlert : gpsAlerts) {
            Future<?> task = executorService.submit(() -> readGpsData(gpsAlert));
            gpsAlertTasks.put(gpsAlert.getDogId(), task); // 새로운 작업을 Map에 저장
        }
    }

    // 두 GPS 좌표를 받아 거리 계산 후 SMS 전송 여부 결정
    public void checkDistanceAndSendSms(double lat1, double lon1, GpsAlert gpsAlert) {

        double lat2 = gpsAlert.getLatitude();
        double lon2 = gpsAlert.getLongitude();
        String dogName = gpsAlert.getExtra__dogName();
        int chack = gpsAlert.getChack();

        double distance = calculateDistance(lat1, lon1, lat2, lon2); // 거리 계산

        if (distance > 0.5 && chack == 0) { // 500m 이상 떨어져 있을 때
            // sendSms("아이고! " + dogName + "(이)가 정해진 장소를 떠났네요. 위치를 확인해 주세요!", gpsAlert); // SMS 전송
            gpsAlertRepository.toggleChack(1, gpsAlert.getId());
            chack = 1;
        } else if (distance <= 1.0) { // 다시 구역 안으로 들어왔을때
            gpsAlertRepository.toggleChack(0, gpsAlert.getId());
            chack = 0;
        }
    }

    // 두 지점의 위도와 경도를 받아 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1); // 위도 차이 (라디안 단위로 변환)
        double lonDistance = Math.toRadians(lon2 - lon1); // 경도 차이 (라디안 단위로 변환)

        // 하버사인 공식을 사용하여 두 지점 간의 거리 계산
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // 구면 삼각법을 이용한 거리 계산
        return EARTH_RADIUS * c; // 계산된 거리를 km 단위로 반환
    }

    // SMS를 전송하는 메서드
    private void sendSms(String messageContent, GpsAlert gpsAlert) {
        String userCellphoneNum = gpsAlert.getExtra__cellphoneNum();

        // String toNumber = userCellphoneNum; // 수신자 번호
        String toNumber = "01022296877"; // 수신자 번호
        String fromNumber = "01022296877";  // 발신 번호 (등록된 번호)

        // Coolsms 객체 생성
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        // DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.nurigo.net");

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText(messageContent); // 전송할 메시지 내용

        try {
            // SMS 전송
            messageService.send(message);
            System.out.println("메시지가 성공적으로 전송되었습니다.");
        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다
            System.out.println(exception.getFailedMessageList());
            System.out.println("메시지 발송 실패: " + exception.getMessage());
        } catch (Exception e) {
            // 일반적인 예외 처리
            System.err.println("오류 발생: " + e.getMessage());
        }
    }


    /**
     * 아두이노 GPS 트래커에서 데이터를 읽고 처리하는 메소드
     * comPortName : 아두이노 COM 포트 이름 (예: "COM7")
     * lat2 : 비교할 고정된 위도
     * lon2 : 비교할 고정된 경도
     */
    public void readGpsData(GpsAlert gpsAlert) {
        SerialPort comPort = SerialPort.getCommPort(gpsAlert.getExtra__comPortName());
        comPort.setComPortParameters(9600, 8, 1, 0);
        comPort.openPort();

        int number = 1;

        if (gpsAlert.getOnOff() == 1) {
            System.err.println(gpsAlert.getExtra__comPortName()+" : gps가 꺼져있습니다");
            return;
        }else {
            System.err.println(gpsAlert.getExtra__comPortName() + " 연결되었습니다.");
        }

        try {
            while (!Thread.currentThread().isInterrupted()) {
                // System.err.println(gpsAlert.getExtra__comPortName() + " 실행중 : " + number + "회");
                number++;

                if (comPort.bytesAvailable() > 0) {
                    byte[] readBuffer = new byte[comPort.bytesAvailable()];
                    int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                    String receivedData = new String(readBuffer, 0, numRead);
                    System.out.println("데이터 수신: " + receivedData);

                    try {
                        String[] gpsData = receivedData.trim().split(",");
                        if (gpsData.length == 2) {
                            double latitude = Double.parseDouble(gpsData[0].trim());
                            double longitude = Double.parseDouble(gpsData[1].trim());

                            System.err.println(latitude);
                            System.err.println(longitude);

                            checkDistanceAndSendSms(latitude, longitude, gpsAlert);
                        } else {
                            System.out.println("유효하지 않은 GPS 데이터 형식입니다.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("GPS 데이터 파싱 실패: " + e.getMessage());
                    }
                }

                // 5초 대기 후 다시 데이터 읽기 시도
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // 인터럽트 발생 시 루프를 빠져나옴
                    Thread.currentThread().interrupt(); // 다시 인터럽트 상태 설정
                    break;
                }
            }
        } finally {
            comPort.closePort(); // 포트 종료 처리
            System.err.println(gpsAlert.getExtra__comPortName() + " 포트가 닫혔습니다.");
        }
    }


    public void saveLocation(int dogId, double latitude, double longitude) {
        // System.err.println("서비스 : " + latitude + ", " + longitude);
        gpsAlertRepository.saveLocation(dogId, latitude, longitude);
    }

    public GpsAlert getGpsAlert(int dogId) {
        return gpsAlertRepository.getGpsAlert(dogId);
    }

    public void updateLocation(int dogId, double latitude, double longitude) {
        gpsAlertRepository.updateLocation(dogId, latitude, longitude);
    }

    public void deleteLocation(int dogId) {
        gpsAlertRepository.deleteLocation(dogId);
    }


    public String getPlaceName(double lat, double lng) {
        String url = GEOCODING_API_URL + "?latlng=" + lat + "," + lng + "&key=" + API_KEY + "&language=ko";
        RestTemplate restTemplate = new RestTemplate();

        // API 호출
        String response = restTemplate.getForObject(url, String.class);

        // JSON 파싱
        JSONObject json = new JSONObject(response);
        if ("OK".equals(json.getString("status"))) {
            return json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
        } else {
            return "Address not found";
        }
    }

    public List<GpsAlert> All() {
        return gpsAlertRepository.All();
    }

    public void toggleOnOff(int dogId, int value) {
        gpsAlertRepository.toggleOnOff(dogId, value);
        restartGpsDataListenerForDog(dogId);
    }
}

