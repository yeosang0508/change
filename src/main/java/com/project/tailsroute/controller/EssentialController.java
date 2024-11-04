package com.project.tailsroute.controller;

import com.project.tailsroute.service.EssentialService;
import com.project.tailsroute.vo.Essentials;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usr/essential")
public class EssentialController {

    private final EssentialService essentialService;

    @Autowired
    public EssentialController(EssentialService essentialService) {
        this.essentialService = essentialService;
    }

    @PostMapping("/add")
    public String addEssential(@RequestBody Essentials essential) {
        essentialService.saveEssential(essential);
        return "Essential added successfully";
    }
    @GetMapping("/get")
    public List<Essentials> getEssentials(@RequestParam int memberId) {
        return essentialService.findEssentialsByMemberId(memberId);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEssentials(@RequestParam int id) {
        essentialService.deleteEssentials(id);
        return ResponseEntity.ok("{\"message\":\"삭제 성공\"}"); // 성공 메시지 포함
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateEssentials(@RequestBody Essentials essentials) {
        essentialService.updateEssentials(
                essentials.getItemType(),
                essentials.getUsageCycle(),
                essentials.getTiming(),
                essentials.getPurchaseDate(),
                essentials.getId()
        );
        return ResponseEntity.ok("{\"message\":\"수정 성공\"}");
    }
}