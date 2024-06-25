package com.arias_code.ecom.service.auth;

import com.arias_code.ecom.dto.SignupDTO;
import com.arias_code.ecom.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupDTO signupDTO);
    boolean hasUserWithEmail(String email);
}
