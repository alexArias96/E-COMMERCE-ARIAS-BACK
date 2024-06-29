package com.arias_code.ecom.controller;

import com.arias_code.ecom.dto.AuthenticationRequest;
import com.arias_code.ecom.dto.SignupDTO;
import com.arias_code.ecom.dto.UserDto;
import com.arias_code.ecom.entity.User;
import com.arias_code.ecom.repository.UserRepository;
import com.arias_code.ecom.service.auth.AuthService;
import com.arias_code.ecom.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;


    private final AuthService authService;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                            HttpServletResponse response)
            throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, JSONException, ServletException {

        try {
            authenticationManager.
                    authenticate(
                            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername()
                            ,authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Optional<User> userOptional = userRepository.findFirstByEmail(userDetails.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (userOptional.isPresent()){
            response.getWriter().write(
                    new JSONObject()
                            .put("userId", userOptional.get().getId())
                            .put("role", userOptional.get().getRole())
                            .toString()
            );

            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers",
                    "Authorization, X-PINGOTHER, ORIGIN, " + "X-Requested-With, Content-type, Accept, X-Custom-header");

            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO){
        if (authService.hasUserWithEmail(signupDTO.getEmail())){
            return new ResponseEntity<>("Username already exist", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(signupDTO);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
