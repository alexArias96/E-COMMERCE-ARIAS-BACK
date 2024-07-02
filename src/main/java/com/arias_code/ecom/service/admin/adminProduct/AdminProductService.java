package com.arias_code.ecom.service.admin.adminProduct;

import com.arias_code.ecom.dto.ProductDTO;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {
    ProductDTO addProduct(ProductDTO productDTO) throws IOException;
    List<ProductDTO> getAllProducts();
}
