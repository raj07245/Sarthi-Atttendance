package com.smart.face.attendance.controller;

import com.smart.face.attendance.dto.UserRequest;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.security.JwtUtil;
import com.smart.face.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // ✅ Signup with DTO (organization assign)
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody UserRequest req) {
        userService.signup(req);
        return "User registered";
    }

    // ✅ Login returns JWT
    @PostMapping("/login")
    public String login(@RequestBody UserRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        UserDetailsImpl userDetails = userService.loadUserByUsername(req.getEmail());
        return jwtUtil.generateToken(userDetails);
    }
}