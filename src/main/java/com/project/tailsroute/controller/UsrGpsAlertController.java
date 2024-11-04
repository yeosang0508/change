package com.project.tailsroute.controller;


import com.project.tailsroute.service.GpsAlertService;
import com.project.tailsroute.vo.GpsAlert;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
public class UsrGpsAlertController {

    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;

    private final Rq rq;

    public UsrGpsAlertController(Rq rq) {
        this.rq = rq;
    }

    @Autowired
    private GpsAlertService gpsAlertService;

    // 프로젝트 시작 시 GPS 데이터 수신을 시작하는 메서드 (한번만 실행됨)
    @PostConstruct
    public void startGpsDataListener() {
        gpsAlertService.startGpsDataListener();
    }

    @GetMapping("/usr/gpsAlert/add")
    public String showAdd(Model model, @RequestParam("dogId") int dogId) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        model.addAttribute("GOOGLE_MAP_API_KEY", API_KEY);
        model.addAttribute("isLogined", isLogined);
        model.addAttribute("dogId", dogId);

        return "/usr/gpsAlert/add";
    }

    @GetMapping("/usr/gpsAlert/modify")
    public String showModify(Model model, @RequestParam("dogId") int dogId) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        GpsAlert gpsAlert = gpsAlertService.getGpsAlert(dogId);

        if (gpsAlert != null) {
            double latitude = gpsAlert.getLatitude();
            double longitude = gpsAlert.getLongitude();
            model.addAttribute("latitude", latitude);
            model.addAttribute("longitude", longitude);
        }

        model.addAttribute("GOOGLE_MAP_API_KEY", API_KEY);
        model.addAttribute("isLogined", isLogined);
        model.addAttribute("dogId", dogId);

        return "/usr/gpsAlert/modify";
    }

    @PostMapping("/usr/gpsAlert/saveLocation")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public ResponseEntity<String> saveLocation(
            @RequestParam int dogId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        // System.err.println("컨트롤러 : " + latitude + ", " + longitude);
        try {
            gpsAlertService.saveLocation(dogId, latitude, longitude);
            gpsAlertService.restartGpsDataListener();
            return ResponseEntity.ok("위치가 등록되었습니다");
        } catch (Exception e) {
            e.printStackTrace(); // 에러 메시지를 콘솔에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("위치 추가에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/usr/gpsAlert/updateLocation")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public ResponseEntity<String> updateLocation(
            @RequestParam int dogId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        // System.err.println("컨트롤러 : " + latitude + ", " + longitude);
        try {
            gpsAlertService.updateLocation(dogId, latitude, longitude);
            gpsAlertService.restartGpsDataListener();
            return ResponseEntity.ok("위치가 수정되었습니다");
        } catch (Exception e) {
            e.printStackTrace(); // 에러 메시지를 콘솔에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("위치 수정에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/usr/gpsAlert/deleteLocation")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public ResponseEntity<String> deleteLocation(
            @RequestParam int dogId) {
        try {
            gpsAlertService.deleteLocation(dogId);
            gpsAlertService.restartGpsDataListener();
            return ResponseEntity.ok("위치가 삭제되었습니다");
        } catch (Exception e) {
            e.printStackTrace(); // 에러 메시지를 콘솔에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("위치 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/usr/gpsAlert/test")
    public String showAdd(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        model.addAttribute("isLogined", isLogined);

        return "/usr/gpsAlert/test";
    }

    @GetMapping("/usr/gpsAlert/toggleOnOff")
    public String toggleGps(@RequestParam("dogId") int dogId, @RequestParam("value") int value) {
        if (value == 1) {
            // GPS 켜기 로직
            gpsAlertService.toggleOnOff(dogId, value);
            return "redirect:/usr/member/myPage";
        } else {
            // GPS 끄기 로직
            gpsAlertService.toggleOnOff(dogId, value);
            return "redirect:/usr/member/myPage";
        }
    }
}
