package com.project.tailsroute.service;


import com.project.tailsroute.repository.GpsChackRepository;
import com.project.tailsroute.vo.GpsChack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpsChackService {

    @Autowired
    GpsChackRepository gpsChackRepository;

    public GpsChack chack(int memberId) {
        return gpsChackRepository.chack(memberId);
    }

    public void on(double latitude, double longitude, String location, int memberId) {
        gpsChackRepository.on(latitude, longitude, location, memberId);
    }

    public void off(int memberId) {
        gpsChackRepository.off(memberId);
    }

    public void update(double latitude, double longitude, String location, int memberId) {
        gpsChackRepository.update(latitude, longitude, location, memberId);
    }

    public int[] getRegionCode(String[] locations) {
        return gpsChackRepository.getRegionCode(locations[0], locations[1]);
    }
}

