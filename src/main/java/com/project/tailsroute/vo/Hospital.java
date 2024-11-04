package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
    private int id;  // 병원 ID
    private String name;  // 병원 이름
    private String callNumber;  // 전화번호
    private String jibunAddress;  // 지번 주소
    private String roadAddress;  // 도로명 주소
    private String latitude;  // 위도
    private String longitude;  // 경도
    private String businessStatus;  // 영업 상태 (영업/폐업)
    private String type;  // 병원 타입 (일반/야간/24시간)
}
