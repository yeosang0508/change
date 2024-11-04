package com.project.tailsroute.service;

import com.project.tailsroute.repository.AlarmRepository;
import com.project.tailsroute.vo.Alarms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Autowired
    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public void saveAlarm(Alarms alarm) {
        // Repository를 사용하여 Essential 객체를 저장
        alarmRepository.addAlarms(
                alarm.getMemberId(),
                alarm.getAlarm_date(),
                alarm.getMessage(),
                alarm.getSite()
        );
    }
    public List<Alarms> findAlarmsByMemberId(int memberId) {
        // memberId에 해당하는 Essentials를 데이터베이스에서 조회하여 반환
        return alarmRepository.findByMemberId(memberId);
    }
    public void updateAlarms(String alarm_date ,String message , int id) {
        alarmRepository.updateAlarms(alarm_date, message,id);
    }
    public void deleteAlarms(int id) {
        alarmRepository.deleteAlarms(id);
    }
}
