package com.project.tailsroute.service;

import com.project.tailsroute.repository.DogRepository;
import com.project.tailsroute.repository.MissingRepository;
import com.project.tailsroute.vo.Missing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissingService {
    @Autowired
    private MissingRepository missingRepository;

    public int lastNumber() {
        Integer number = missingRepository.lastNumber();
        return (number != null) ? number : 0; // null일 경우 0을 반환
    }

    public void write(int loginedMemberId, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait) {
        missingRepository.write(loginedMemberId, name, reportDate, missingLocation, breed, color, gender, age, rfid, photoPath, trait);
    }

    public int totalCnt(String str) {
        return missingRepository.totalCnt(str);
    }

    public List<Missing> list(int limitFrom, int itemsInAPage, String str) {
        String str2 = "전체";
        if (str.equals("인천광역시")) {
            str2 = "인천";
        } else if (str.equals("서울특별시")) {
            str2 = "서울";
        } else if (str.equals("경기도")) {
            str2 = "경기";
        } else if (str.equals("강원도")) {
            str2 = "강원";
        } else if (str.equals("충청남도")) {
            str2 = "충남";
        } else if (str.equals("세종특별자치시")) {
            str2 = "세종";
        } else if (str.equals("대전광역시")) {
            str2 = "대전";
        } else if (str.equals("충청북도")) {
            str2 = "충북";
        } else if (str.equals("전라북도")) {
            str2 = "전북";
        } else if (str.equals("대구광역시")) {
            str2 = "대구";
        } else if (str.equals("울산광역시")) {
            str2 = "울산";
        } else if (str.equals("경상북도")) {
            str2 = "경북";
        } else if (str.equals("전라남도")) {
            str2 = "전남";
        } else if (str.equals("광주광역시")) {
            str2 = "광주";
        } else if (str.equals("경상남도")) {
            str2 = "경남";
        } else if (str.equals("부산광역시")) {
            str2 = "부산";
        } else if (str.equals("제주특별자치도")) {
            str2 = "제주";
        }
        // System.err.println(str + ", " + str2);
        return missingRepository.list(limitFrom, itemsInAPage, str, str2);
    }

    public Missing missingArticle(int missingId) {
        return missingRepository.missingArticle(missingId);
    }

    public void missingDelete(int missingId) {
        missingRepository.missingDelete(missingId);
    }

    public void modify(int id, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait) {
        missingRepository.modify(id, name, reportDate, missingLocation, breed, color, gender, age, rfid, photoPath, trait);
    }

    public String[] getRegionCode(String missingLocation) {
        if (missingLocation.contains("인천")) return new String[]{"인천", "인천광역시"};
        if (missingLocation.contains("서울")) return new String[]{"서울", "서울특별시"};
        if (missingLocation.contains("경기")) return new String[]{"경기", "경기도"};
        if (missingLocation.contains("강원")) return new String[]{"강원", "강원도"};
        if (missingLocation.contains("충남") || missingLocation.contains("충청남도") ) return new String[]{"충남", "충청남도"};
        if (missingLocation.contains("세종")) return new String[]{"세종", "세종특별자치시"};
        if (missingLocation.contains("대전")) return new String[]{"대전", "대전광역시"};
        if (missingLocation.contains("충북") || missingLocation.contains("충청북도")) return new String[]{"충북", "충청북도"};
        if (missingLocation.contains("전북") || missingLocation.contains("전라북도")) return new String[]{"전북", "전라북도"};
        if (missingLocation.contains("대구")) return new String[]{"대구", "대구광역시"};
        if (missingLocation.contains("울산")) return new String[]{"울산", "울산광역시"};
        if (missingLocation.contains("경북") || missingLocation.contains("경상북도")) return new String[]{"경북", "경상북도"};
        if (missingLocation.contains("전남") || missingLocation.contains("전라남도")) return new String[]{"전남", "전라남도"};
        if (missingLocation.contains("광주")) return new String[]{"광주", "광주광역시"};
        if (missingLocation.contains("경남") || missingLocation.contains("경상남도")) return new String[]{"경남", "경상남도"};
        if (missingLocation.contains("부산")) return new String[]{"부산", "부산광역시"};
        if (missingLocation.contains("제주")) return new String[]{"제주", "제주특별자치도"};
        return null;
    }

    public int findMissingId() {
        return missingRepository.findMissingId();
    }
}
