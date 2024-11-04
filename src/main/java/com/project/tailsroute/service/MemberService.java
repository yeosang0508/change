package com.project.tailsroute.service;

import com.project.tailsroute.repository.MemberRepository;
import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member getMemberByLoginId(String loginId) {
        return memberRepository.getMemberByLoginId(loginId);
    }

    public Member getMemberById(int id) {
        return memberRepository.getMemberById(id);
    }



    public ResultData<Integer> signUp(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
                                      String email) {
        Member existsMember = getMemberByLoginId(loginId);

        if (existsMember != null) {
            return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다.", loginId));
        }


        memberRepository.doSignUp(loginId, loginPw, name, nickname, cellphoneNum, email);

        int id = memberRepository.getLastInsertId();

        return ResultData.from("S-1", "회원가입 성공", "생성된 회원 id", id);
    }


}
