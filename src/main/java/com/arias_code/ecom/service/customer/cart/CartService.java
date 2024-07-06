package com.arias_code.ecom.service.customer.cart;

import com.arias_code.ecom.dto.AddProductInCartDTO;
import com.arias_code.ecom.dto.OrderDTO;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addProductToCart(AddProductInCartDTO addProductInCartDTO);
    OrderDTO getCartByUserId(Long userId);
    OrderDTO applyCoupon(Long userId, String code);
}
