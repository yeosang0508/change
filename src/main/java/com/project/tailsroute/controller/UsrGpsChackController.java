package com.project.tailsroute.controller;


import com.project.tailsroute.service.GpsAlertService;
import com.project.tailsroute.service.GpsChackService;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UsrGpsChackController {

    private final Rq rq;

    public UsrGpsChackController(Rq rq) {
        this.rq = rq;
    }

    @Autowired
    private GpsChackService gpsChackService;

    @Autowired
    private GpsAlertService gpsAlertService;

    @PostMapping("/usr/gpsChack/on")
    public ResponseEntity<?> gpsChackOn(@RequestParam double latitude, @RequestParam double longitude) {
        Member member = rq.getLoginedMember();

        String location = gpsAlertService.getPlaceName(latitude, longitude);
        location = location.substring(5);

        gpsChackService.on(latitude, longitude, location, member.getId());

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/usr/gpsChack/off")
    public ResponseEntity<?> gpsChackOff() {
        Member member = rq.getLoginedMember();

        gpsChackService.off(member.getId());

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/usr/gpsChack/update")
    public ResponseEntity<?> gpsChackUpdate(@RequestParam double latitude, @RequestParam double longitude) {
        Member member = rq.getLoginedMember();

        String location = gpsAlertService.getPlaceName(latitude, longitude);
        location = location.substring(5);

        gpsChackService.update(latitude, longitude, location, member.getId());

        return ResponseEntity.ok(Map.of("success", true));
    }

}
