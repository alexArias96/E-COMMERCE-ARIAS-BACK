package com.arias_code.ecom.dto;

import lombok.Data;

@Data
public class AddProductInCartDTO {
    private Long userId;
    private Long productId;
}
