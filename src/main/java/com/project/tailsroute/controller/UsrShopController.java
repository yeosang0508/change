package com.project.tailsroute.controller;

import com.project.tailsroute.service.ArticleService;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsrShopController {
    private final Rq rq;
    public UsrShopController(Rq rq){this.rq = rq;}
    @Autowired
    private ArticleService articleService;

    @GetMapping("/usr/shopping/page")
    public String showMain(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        } else{
            return "redirect:/usr/member/login";
        }

        model.addAttribute("isLogined", isLogined);
        return "usr/shopping/page";
    }

    @GetMapping("/usr/shopping/write")
    public String write(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        } else {
            return "redirect:/usr/member/login";
        }

        model.addAttribute("isLogined", isLogined);
        Integer currentId = articleService.getCurrentArticleId();
        if (currentId == null) {
            currentId = 1; // 기본값 설정
        }

        model.addAttribute("currentId", currentId);
        return "usr/shopping/write"; // JSP 파일 경로
    }
}