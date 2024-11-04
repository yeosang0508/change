package com.project.tailsroute.service;

import com.project.tailsroute.repository.DogRepository;
import com.project.tailsroute.vo.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    public int lastNumber() {
        Integer number = dogRepository.lastNumber();
        return (number != null) ? number : 0; // null일 경우 0을 반환
    }

    public void upload(int loginedMemberId, String dogName, String dogWeight, String dogType, String photoPath) {
        if (dogWeight.isEmpty()) dogWeight = "모름";
        dogRepository.upload(loginedMemberId, dogName, dogWeight, dogType, photoPath);
    }

    public Dog getDogfile(int loginedMemberId) {
        return dogRepository.getDogfile(loginedMemberId);
    }

    public Dog getDogfileId(int dogId) {
        return dogRepository.getDogfileId(dogId);
    }

    public void modify(int dogId, String dogName, String dogWeight, String dogType, String photoPath) {
        if (dogWeight.isEmpty()) dogWeight = "모름";
        dogRepository.modify(dogId, dogName, dogWeight, dogType, photoPath);
    }
}
