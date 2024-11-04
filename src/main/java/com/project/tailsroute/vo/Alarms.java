package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alarms {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private String alarm_date;
    private String alarm_day;
    private String message;
    private String site;
    private String is_sent;
}
