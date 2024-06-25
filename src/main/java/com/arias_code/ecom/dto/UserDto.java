package com.arias_code.ecom.dto;

import com.arias_code.ecom.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
}
