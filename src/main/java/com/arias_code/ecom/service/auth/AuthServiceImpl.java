package com.arias_code.ecom.service.auth;

import com.arias_code.ecom.dto.SignupDTO;
import com.arias_code.ecom.dto.UserDto;
import com.arias_code.ecom.entity.Order;
import com.arias_code.ecom.entity.User;
import com.arias_code.ecom.enums.OrderStatus;
import com.arias_code.ecom.enums.UserRole;
import com.arias_code.ecom.repository.OrderRepository;
import com.arias_code.ecom.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    public UserDto createUser(SignupDTO signupDTO){
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        /*
         * when the user is created the initial order is also created
         */
        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if (null == adminAccount){
            User user = new User();
            user.setName("admin");
            user.setEmail("admin@admin.com");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin1"));
            userRepository.save(user);
        }
    }
}
