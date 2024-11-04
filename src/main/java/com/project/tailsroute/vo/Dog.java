package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dog {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private String name;
    private String weight;
    private String photo;
    private String type;
    private String comPortName;
}