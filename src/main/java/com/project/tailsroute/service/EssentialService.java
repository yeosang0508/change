package com.project.tailsroute.service;

import com.project.tailsroute.repository.EssentialsRepository;
import com.project.tailsroute.vo.Essentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EssentialService {

    private final EssentialsRepository essentialsRepository;

    @Autowired
    public EssentialService(EssentialsRepository essentialsRepository) {
        this.essentialsRepository = essentialsRepository;
    }

    public void saveEssential(Essentials essential) {
        // Repository를 사용하여 Essential 객체를 저장
        essentialsRepository.addEssentials(
                essential.getMemberId(),
                essential.getItemType(),
                essential.getPurchaseDate(),
                essential.getUsageCycle(),
                essential.getTiming()
        );
    }
    public List<Essentials> findEssentialsByMemberId(int memberId) {
        // memberId에 해당하는 Essentials를 데이터베이스에서 조회하여 반환
        return essentialsRepository.findByMemberId(memberId);
    }
    public void updateEssentials(String itemType, int usageCycle, int timing,String purchaseDate,int id) {
        essentialsRepository.updateEssentials(itemType, usageCycle, timing,purchaseDate,id);
    }
    public void deleteEssentials(int id) {
        essentialsRepository.deleteEssentials(id);
    }
}