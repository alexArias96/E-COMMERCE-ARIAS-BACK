package com.arias_code.ecom.controller.admin;

import com.arias_code.ecom.dto.ProductDTO;
import com.arias_code.ecom.service.admin.adminProduct.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> addProduct(@ModelAttribute ProductDTO productDTO) throws IOException {
        ProductDTO productDTO1 = adminProductService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO1);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        List<ProductDTO> productDTOS = adminProductService.getAllProducts();
        return ResponseEntity.ok(productDTOS);
    }
}
