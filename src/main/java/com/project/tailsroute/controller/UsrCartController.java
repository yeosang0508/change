package com.project.tailsroute.controller;

import com.project.tailsroute.service.CartService;
import com.project.tailsroute.vo.Carts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Lists;

import java.util.List;

@RestController
@RequestMapping("/usr/cart")
public class UsrCartController {
    private final CartService cartService;

    @Autowired
    public UsrCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addCart(@RequestBody Carts cart) {
        cartService.saveCart(cart);
        return "Cart added successfully";
    }
    @GetMapping("/get")
    public List<Carts> getCarts(@RequestParam int memberId) {
        return cartService.findCartsByMemberId(memberId);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCart(@RequestParam int id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok("{\"message\":\"삭제 성공\"}"); // 성공 메시지 포함
    }
}
