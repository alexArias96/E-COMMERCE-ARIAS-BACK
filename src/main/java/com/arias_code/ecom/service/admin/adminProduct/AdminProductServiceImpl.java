package com.arias_code.ecom.service.admin.adminProduct;

import com.arias_code.ecom.dto.ProductDTO;
import com.arias_code.ecom.entity.Category;
import com.arias_code.ecom.entity.Product;
import com.arias_code.ecom.repository.CategoryRepository;
import com.arias_code.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDTO addProduct(ProductDTO productDTO) throws IOException {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImg(productDTO.getImg().getBytes());

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow();

        product.setCategory(category);

        return productRepository.save(product).getDto();
    }

    public List<ProductDTO> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
}
