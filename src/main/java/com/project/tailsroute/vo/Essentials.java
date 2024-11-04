package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Essentials {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private String itemType;
    private String purchaseDate;
    private int usageCycle;
    private int timing;
    private int purchaseStatus;
}
