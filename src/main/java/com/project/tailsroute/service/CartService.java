package com.project.tailsroute.service;

import com.project.tailsroute.repository.CartRepository;
import com.project.tailsroute.vo.Carts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void saveCart(Carts cart) {
        cartRepository.addCarts(
                cart.getMemberId(),
                cart.getItemName(),
                cart.getItemprice(),
                cart.getItemlink()
        );
    }
    public List<Carts> findCartsByMemberId(int memberId) {
        return cartRepository.findByMemberId(memberId);
    }
    public void deleteCart(int id) {
        cartRepository.deleteCart(id);
    }
}