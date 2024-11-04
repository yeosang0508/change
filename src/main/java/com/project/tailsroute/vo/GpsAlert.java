package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpsAlert {
    private int id;
    private int dogId;
    private double latitude;
    private double longitude;
    private int chack;
    private int onOff;

    private String extra__comPortName;
    private String extra__dogName;
    private String extra__cellphoneNum;
}