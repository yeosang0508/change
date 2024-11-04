package com.project.tailsroute.service;

import com.project.tailsroute.repository.DiaryRepository;

import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.Diary;
import com.project.tailsroute.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Transactional
    public void writeDiary(int memberId, String title, String body, String imagePath,
                                 LocalDate startDate, LocalDate endDate,
                                 LocalTime takingTime, String information) {

        diaryRepository.writeDiary(memberId, title, body, imagePath, startDate, endDate, takingTime, information);
    }

    public List<Diary> getDiaryList(int memberId,String sort, int page, int size) {
        int offset = (page - 1) * size;
        int limit = size;

        if ("oldest".equals(sort)) {
            return diaryRepository.findAllByOrderByDateDesc(memberId,limit, offset); // 오래된순
        } else {
            return diaryRepository.findAllByOrderByDateAsc(memberId,limit, offset); // 최신순
        }

    }

    public int countDiaries(int memberId) {
        return diaryRepository.countDiaries(memberId);

    }

    public void deleteDiary(int id) {
        diaryRepository.deleteDiary(id);
    }

    public void modifyDiary(int id, String title, String body, String imagePath,
                            LocalDate startDate, LocalDate endDate,
                            LocalTime takingTime, String information) {
        diaryRepository.modifyDiary(id, title, body, imagePath, startDate, endDate, takingTime, information);
    }

    public Diary getDiaryById(int id) {
        return diaryRepository.getDiaryById(id);
    }

    public Diary getForPrintDiary(int id) {
        return diaryRepository.getForPrintDiary(id);
    }

    public List<Diary> findAllDiary(int memberId){
        return diaryRepository.findAllDiary(memberId);
    }
}