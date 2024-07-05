package com.arias_code.ecom.service.customer;

import com.arias_code.ecom.dto.ProductDTO;

import java.util.List;

public interface CustomerProductService {

    List<ProductDTO> getAllProducts();
    List<ProductDTO> searchProductByTitle(String title);
}
