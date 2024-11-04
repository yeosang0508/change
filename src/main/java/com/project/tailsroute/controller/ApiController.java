package com.project.tailsroute.controller;

import com.project.tailsroute.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/searchProducts")
    public ResponseEntity<?> searchProducts(@RequestParam(name = "query") String query) {
        String result = apiService.searchProducts(query);
        return ResponseEntity.ok(result);
    }
}