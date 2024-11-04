package com.project.tailsroute.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private int id;
    private String regDate;
    private String updateDate;
    private String loginId;
    private String loginPw;
    private int authLevel;
    private String name;
    private String nickname;
    private int gender;
    private String cellphoneNum;
    private boolean delStatus;
    private String delDate;

    private String extra__dogPoto;


    public boolean isAdmin() {
        return this.authLevel == 7;
    }

}