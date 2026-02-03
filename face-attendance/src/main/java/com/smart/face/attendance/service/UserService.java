package com.smart.face.attendance.service;

import com.smart.face.attendance.dto.SignupRequest;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already registered";
        }

        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "User registered Succesfully";
    }

    public String login(String email , String password){

        User user= userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        return "LOGIN_SUCCESS";
    }


}
