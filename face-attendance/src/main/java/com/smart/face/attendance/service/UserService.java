package com.smart.face.attendance.service;

import com.smart.face.attendance.dto.UserRequest;
import com.smart.face.attendance.entity.Organization;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.OrganizationRepository;
import com.smart.face.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Signup with organization assign
    public User signup(UserRequest req) {

        // 1️⃣ Fetch Organization by ID
        Organization org = organizationRepository.findById(req.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // 2️⃣ Build User entity
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .organization(org)   // ✅ Assign org
                .faceRegistered(false)
                .build();

        // 3️⃣ Save user in DB
        return userRepository.save(user);
    }

    // ✅ Load UserDetailsImpl for JWT authentication
    public UserDetailsImpl loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDetailsImpl(user);
    }
}