package com.project.tailsroute.controller;

import com.project.tailsroute.service.DogService;
import com.project.tailsroute.vo.Dog;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.Missing;
import com.project.tailsroute.vo.Rq;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UsrDogController {
    private final Rq rq;

    public UsrDogController(Rq rq) {
        this.rq = rq;
    }


    @Autowired
    private DogService dogService;

    @GetMapping("/usr/dog/add")
    public String showAdd(Model model) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        model.addAttribute("isLogined", isLogined);

        return "/usr/dog/add";
    }

    @GetMapping("/usr/dog/modify")
    public String showModify(Model model, @RequestParam("dogId") int dogId) {
        boolean isLogined = rq.isLogined();

        Dog dog = dogService.getDogfileId(dogId);

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }

        if (rq.getLoginedMemberId() != dog.getMemberId()) {
            return "redirect:/usr/home/main";
        }

        model.addAttribute("isLogined", isLogined);
        model.addAttribute("dog", dog);

        return "/usr/dog/modify";
    }

    @PostMapping("/usr/dog/doModify")
    public String modify(@RequestParam("id") int id, @RequestParam("dog_name") String dogName, @RequestParam(value = "dog_weight", required = false) String dogWeight, @RequestParam("dog_type") String dogType, @RequestParam("dog_photo") MultipartFile file) {

        Dog dog = dogService.getDogfileId(id);
        String dogPhoto = dog.getPhoto();

        if (rq.getLoginedMemberId() != dog.getMemberId()) {
            return "redirect:/usr/home/main";
        }

        // 파일 처리 로직
        String photoPath = null;
        if (!file.isEmpty()) {

            String filePath = "src/main/resources/static/resource/photo/dog" + id + ".png";
            try {
                // 파일 저장 전에 이미지 크기 조절
                Thumbnails.of(file.getInputStream()).size(80, 80) // 원하는 사이즈로 조정
                        .toFile(new File(filePath));

                photoPath = "/resource/photo/dog" + id + ".png"; // 웹에서 접근할 수 있는 경로
            } catch (IOException e) {
                return "redirect:/usr/home/main;";
            }
            // 데이터베이스에 반려견 정보 수정
            dogService.modify(id, dogName, dogWeight, dogType, photoPath);
        }else dogService.modify(id, dogName, dogWeight, dogType, dogPhoto);



        return "redirect:/usr/member/myPage";
    }


    @PostMapping("/usr/dog/upload")
    public String upload(@RequestParam("dog_name") String dogName, @RequestParam(value = "dog_weight", required = false) String dogWeight, @RequestParam("dog_type") String dogType, @RequestParam("dog_photo") MultipartFile file) {

        // 파일 처리 로직
        String photoPath = null;
        if (!file.isEmpty()) {
            int number = dogService.lastNumber(); // 데이터베이스에서 가져온 마지막 ID
            number++;

            String filePath = "src/main/resources/static/resource/photo/dog" + number + ".png";
            try {
                // 파일 저장 전에 이미지 크기 조절
                Thumbnails.of(file.getInputStream()).size(80, 80) // 원하는 사이즈로 조정
                        .toFile(new File(filePath));

                photoPath = "/resource/photo/dog" + number + ".png"; // 웹에서 접근할 수 있는 경로
            } catch (IOException e) {
                return "redirect:/usr/dog/add";
            }
        }

        // 데이터베이스에 반려견 정보 저장
        dogService.upload(rq.getLoginedMemberId(), dogName, dogWeight, dogType, photoPath);

        return "redirect:/usr/home/main"; // 메인 페이지로 리다이렉트
    }

}