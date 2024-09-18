package com.backend.demo.services.auth;

import com.backend.demo.Entity.User;
import com.backend.demo.dto.SignupRequest;
import com.backend.demo.dto.UserDto;
import com.backend.demo.enums.UserRole;

import com.backend.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Note: Renamed to follow Java naming conventions

    public UserDto createUser(SignupRequest signupRequest){

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Use injected PasswordEncoder
        user.setRole(UserRole.CUSTOMER);

        User createdUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;

    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if (adminAccount == null) { // Use '== null' instead of 'null ==' for clarity
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(passwordEncoder.encode("admin")); // Use injected PasswordEncoder
            userRepository.save(user);
        }
    }
}
