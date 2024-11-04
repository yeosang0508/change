package com.project.tailsroute.controller;

import com.project.tailsroute.service.DogService;
import com.project.tailsroute.service.GpsAlertService;
import com.project.tailsroute.service.MemberService;
import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.Dog;
import com.project.tailsroute.vo.GpsAlert;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UsrMemberController {

    private final Rq rq;

    public UsrMemberController(Rq rq) {
        this.rq = rq;
    }

    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DogService dogService;

    @Autowired
    private GpsAlertService gpsAlertService;

    @GetMapping("/usr/member/login")
    public String showMain(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        model.addAttribute("isLogined", isLogined);


        return "usr/member/login";
    }

    @PostMapping("/usr/member/doLogin")
    @ResponseBody
    public Map<String, Object> doLogin(HttpServletRequest req, @RequestParam("loginId") String loginId, @RequestParam("loginPw") String loginPw) {
        Map<String, Object> response = new HashMap<>();

        if (Ut.isEmptyOrNull(loginId)) {
            response.put("resultCode", "F-1");
            response.put("jsAction", "history.back(); alert('loginId 입력 x');");
            return response;
        }

        if (Ut.isEmptyOrNull(loginPw)) {
            response.put("resultCode", "F-2");
            response.put("jsAction", "history.back(); alert('loginPw 입력 x');");
            return response;
        }

        Member member = memberService.getMemberByLoginId(loginId);
        if (member == null) {
            response.put("resultCode", "F-3");
            response.put("jsAction", String.format("history.back(); alert('%s는(은) 존재하지 않습니다.');", loginId));
            return response;
        }

        if (!member.getLoginPw().equals(loginPw)) {
            response.put("resultCode", "F-4");
            response.put("jsAction", "history.back(); alert('비밀번호가 틀렸습니다.');");
            return response;
        }

        rq.login(member);
        response.put("resultCode", "S-1");
        response.put("jsAction", String.format("location.href = '/usr/home/main'; alert('%s님 환영합니다.');", member.getNickname()));
        return response;
    }


    @RequestMapping("/usr/member/doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest req) {

        rq.logout();

        return Ut.jsReplace("S-1", Ut.f("로그아웃 되었습니다"), "/usr/home/main");
    }

    @GetMapping("/usr/member/myPage")
    public String showMyPage(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        Dog dog = dogService.getDogfile(rq.getLoginedMemberId());

        boolean locationChack;

        if (dog != null) {
            GpsAlert gpsAlert = gpsAlertService.getGpsAlert(dog.getId());
            if (gpsAlert == null) {
                locationChack = false;
            }else {
                locationChack = true;

                String location = gpsAlertService.getPlaceName(gpsAlert.getLatitude(), gpsAlert.getLongitude());
                location = location.substring(5);
                model.addAttribute("location", location);
            }
            model.addAttribute("locationChack", locationChack);
        }

        model.addAttribute("isLogined", isLogined);
        model.addAttribute("dog", dog);

        return "usr/member/myPage";
    }
}
