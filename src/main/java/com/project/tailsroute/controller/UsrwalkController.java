package com.project.tailsroute.controller;

import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsrwalkController {
    private final Rq rq;
    public UsrwalkController(Rq rq){this.rq = rq;}

    @GetMapping("/usr/walk/page")
    public String showWalk(Model model) {
        boolean isLogined = rq.isLogined();
        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        } else{
            return "redirect:/usr/member/login";
        }

        model.addAttribute("isLogined", isLogined);
        return "usr/walk/page";
    }
}