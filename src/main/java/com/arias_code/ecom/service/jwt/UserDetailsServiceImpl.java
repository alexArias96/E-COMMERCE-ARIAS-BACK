package com.arias_code.ecom.service.jwt;

import com.arias_code.ecom.entity.User;
import com.arias_code.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findFirstByEmail(username);

        if (userOptional.isEmpty())throw new UsernameNotFoundException("Username not found", null);
        return new org.springframework.security.core.userdetails.User(
                userOptional.
                        get().getEmail(),
                userOptional.
                        get().
                        getPassword(),
                new ArrayList<>());
    }
}
