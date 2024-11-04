package com.project.tailsroute.controller;

import com.project.tailsroute.service.AlarmService;
import com.project.tailsroute.vo.Alarms;
import com.project.tailsroute.vo.Essentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usr/alarm")
public class UsrAlarmController {

    private final AlarmService alarmService;

    @Autowired
    public UsrAlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @PostMapping("/add")
    public String addEssential(@RequestBody Alarms alarm) {
        alarmService.saveAlarm(alarm);
        return "Essential added successfully";
    }

    @GetMapping("/get")
    public List<Alarms> getAlarms(@RequestParam int memberId) {
        return alarmService.findAlarmsByMemberId(memberId);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEssentials(@RequestParam int id) {
        alarmService.deleteAlarms(id);
        return ResponseEntity.ok("{\"message\":\"삭제 성공\"}"); // 성공 메시지 포함
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateEssentials(@RequestBody Alarms alarm) {
        alarmService.updateAlarms(
                alarm.getAlarm_date(),
                alarm.getMessage(),
                alarm.getId()
        );
        return ResponseEntity.ok("{\"message\":\"수정 성공\"}");
    }
}