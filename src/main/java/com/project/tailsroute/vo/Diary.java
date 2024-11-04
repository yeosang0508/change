package com.project.tailsroute.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diary {
    private int id;
    private LocalDate regDate;
    private LocalDate updateDate;
    private int memberId;
    private String title;
    private String body;
    private String imagePath;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime takingTime;
    private String information;
    private String extra__writer;

    public Diary(String title, LocalDate startDate) {
    }
}

