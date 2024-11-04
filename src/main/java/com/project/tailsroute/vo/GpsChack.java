package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpsChack {
    private int id;
    private int memberId;
    private String regDate;
    private String updateDate;
    private double latitude;
    private double longitude;
}