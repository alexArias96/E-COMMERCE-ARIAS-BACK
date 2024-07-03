package com.arias_code.ecom.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private long price;
    private byte[] byteImg;
    private Long categoryId;
    private String categoryName;
    private MultipartFile img;
}
