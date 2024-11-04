package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Missing {
    private int id;
    private int memberId;
    private String reportDate;
    private String missingLocation;
    private String breed;
    private String color;
    private String gender;
    private String age;
    private String photo;
    private String name;
    private String RFID;
    private String trait;

    private String extra__ownerName;
    private String extra__ownerCellphoneNum;
}