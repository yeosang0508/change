package com.project.tailsroute.controller;

import com.project.tailsroute.service.DiaryService;
import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.Diary;
import com.project.tailsroute.vo.Member;
import com.project.tailsroute.vo.ResultData;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/usr/diary")
public class UsrDiaryController {

    @Autowired
    private DiaryService diaryService;
    private ResourceLoader resourceLoader;
    private final Rq rq;


    public UsrDiaryController(Rq rq) {
        this.rq = rq;
    }

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        boolean isLogined = rq.isLogined();

        if (!isLogined) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/usr/member/login";
        } else {
            // 로그인된 경우
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
            model.addAttribute("isLogined", true);
        }
        return "usr/diary/write"; // 다이어리 작성 페이지로 이동
    }


        @PostMapping("/write")
        public String submitDiary(
                @RequestParam("memberId") int memberId,
                @RequestParam("title") String title,
                @RequestParam("body") String body,
                @RequestParam("file") MultipartFile file,
                @RequestParam("startDate") String startDateStr, // String으로 받아오기
                @RequestParam("endDate") String endDateStr, // String으로 받아오기
                @RequestParam("takingTime") LocalTime takingTime,
                @RequestParam("information") String information,
                Model model
        ) {

            // DateTimeFormatter 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // String을 LocalDate로 변환
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            String imagePath;

            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();

                // 저장할 디렉토리 경로 설정
                String directoryPath = "src/main/resources/static/resource/DiaryImages";
                File directory = new File(directoryPath);

                // 디렉토리가 존재하지 않으면 생성
                if (!directory.exists()) {
                    directory.mkdirs(); // 디렉토리 생성
                }

                String savePath = new File(directory, fileName).getAbsolutePath();

                System.out.println("File will be saved to: " + savePath);

                try {
                    // 파일을 지정된 경로에 저장
                    file.transferTo(new File(savePath));
                    imagePath = "/resource/DiaryImages/" + fileName; // 웹에서 접근할 수 있는 URL 경로
                } catch (IOException e) {
                    e.printStackTrace();
                    imagePath = "/resource/photo/default.png"; // 기본 이미지 URL로 설정
                }
            } else {
                imagePath = "/resource/photo/default.png"; // 기본 이미지 URL
            }

            // 다이어리 작성 서비스 호출
            diaryService.writeDiary(memberId, title, body, imagePath, startDate, endDate, takingTime, information);
            return "redirect:/usr/diary/list";
        }

    @GetMapping("/list")
    public String showDiaryList(Model model, @RequestParam(defaultValue = "oldest") String sort, @RequestParam(defaultValue = "1") int page) {
        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);

            int memeberId = member.getId();

            int size = 8; // 페이지당 아이템 수
            List<Diary> diaries = diaryService.getDiaryList(memeberId, sort, page, size);
            int totalDiaries = diaryService.countDiaries(memeberId);
            int totalPages = (int) Math.ceil((double) totalDiaries / size);

            model.addAttribute("diaries", diaries);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("sort", sort);

            // pageNumbers 리스트 추가
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 1; i <= totalPages; i++) {
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("isLogined", isLogined);

        return "usr/diary/list";
    }

    @GetMapping("/calendar")
    @ResponseBody // 이 메서드는 JSON으로 반환됨
    public List<Map<String, Object>> getDiaryEvents(Model model) {
        List<Map<String, Object>> events = new ArrayList<>();
        boolean isLogined = rq.isLogined(); // 로그인 여부 확인

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);

            int memberId = member.getId(); // memberId 수정

            List<Diary> diaries = diaryService.findAllDiary(memberId); // 로그인한 사용자 다이어리 항목 가져오기

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            for (Diary diary : diaries) {
                // 일기 이벤트 추가
                Map<String, Object> diaryEvent = new HashMap<>();
                diaryEvent.put("id", diary.getId());
                diaryEvent.put("title", diary.getTitle());
                diaryEvent.put("start", diary.getRegDate().format(dateFormatter)); // 글 작성 날짜
                diaryEvent.put("className","diaryEvent");
                events.add(diaryEvent);

                // 약 복용 이벤트 추가
                Map<String, Object> medicineEvent = new HashMap<>();
                medicineEvent.put("id", diary.getId());
                medicineEvent.put("title", diary.getInformation());

                LocalDateTime startDateTime = LocalDateTime.of(diary.getStartDate(), diary.getTakingTime());
                medicineEvent.put("start", startDateTime.format(dateTimeFormatter)); // 복용 시작일
                LocalDateTime endDateTime = LocalDateTime.of(diary.getEndDate(), diary.getTakingTime());
                medicineEvent.put("end", endDateTime.format(dateTimeFormatter)); // 종료 시간 설정
                diaryEvent.put("className","medicineEvent");
                events.add(medicineEvent);
            }
        } else {
            // 로그인하지 않은 경우 에러 메시지를 추가할 수 있습니다.
            Map<String, Object> errorEvent = new HashMap<>();
            errorEvent.put("error", "로그인 후 이용해주세요.");
            events.add(errorEvent);
        }
        return events; // JSON 형식으로 반환
    }


    @GetMapping("/detail/{id}")
    public String showDiaryDetail(@PathVariable int id, Model model) {

        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }
        model.addAttribute("isLogined", isLogined);

        Diary diary = diaryService.getForPrintDiary(id);
        if (diary == null) {
            // 다이어리가 존재하지 않을 경우, 목록으로 리다이렉트
            return "redirect:/usr/diary/list";
        }

        model.addAttribute("diary", diary);


        return "usr/diary/detail"; // 올바른 뷰 이름
    }

    @PostMapping("/delete/{id}")
    public String deleteDiary(@PathVariable int id) {
        diaryService.deleteDiary(id);
        return "redirect:/usr/diary/list";
    }

    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable int id, Model model) {

        boolean isLogined = rq.isLogined();

        if (isLogined) {
            Member member = rq.getLoginedMember();
            model.addAttribute("member", member);
        }
        model.addAttribute("isLogined", isLogined);

        Diary diary = diaryService.getDiaryById(id);
        model.addAttribute("diary", diary);
        return "usr/diary/modify";
    }

    @PostMapping("/modify/{id}")
    public String modifyDiaryEntry(
            @PathVariable int id,
            @RequestParam("title") String title,
            @RequestParam("body") String body,
            @RequestParam("file") MultipartFile file,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam("takingTime") LocalTime takingTime,
            @RequestParam("information") String information
    ) {

        String imagePath;

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            // 저장할 디렉토리 경로 설정
            String directoryPath = "src/main/resources/static/resource/DiaryImages";
            File directory = new File(directoryPath);

            // 디렉토리가 존재하지 않으면 생성
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            String savePath = new File(directory, fileName).getAbsolutePath();

            System.out.println("File will be saved to: " + savePath);

            try {
                // 파일을 지정된 경로에 저장
                file.transferTo(new File(savePath));
                imagePath = "/resource/DiaryImages/" + fileName; // 웹에서 접근할 수 있는 URL 경로
            } catch (IOException e) {
                e.printStackTrace();
                imagePath = "/resource/photo/default.png"; // 기본 이미지 URL로 설정
            }
        } else {
            imagePath = "/resource/photo/default.png"; // 기본 이미지 URL
        }

        diaryService.modifyDiary(id, title, body, imagePath, startDate, endDate, takingTime, information);
        return "redirect:/usr/diary/detail/" + id;
    }


}