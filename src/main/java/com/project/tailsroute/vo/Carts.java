package com.project.tailsroute.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carts {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private String itemName;
    private int itemprice;
    private String itemlink;
}
