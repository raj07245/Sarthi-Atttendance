package com.smart.face.attendance.controller;


import com.smart.face.attendance.entity.Role;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.PersonRepository;
import com.smart.face.attendance.repository.UserRepository;
import com.smart.face.attendance.service.FaceEngineClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/face")
@RequiredArgsConstructor
public class FaceController {

    private final FaceEngineClient faceEngineClient;
    private final PersonRepository personRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        List<Integer> ids;

        if (user.getRole() == Role.TEACHER) {
            ids = faceEngineClient.registerStudents(user.getId());
        } else if (user.getRole() == Role.MANAGER) {
            ids = faceEngineClient.registerEmployees(user.getId());
        } else {
            return ResponseEntity.status(403).body("Not allowed");
        }

        ids.forEach(id ->
                personRepository.findById(id.longValue()).ifPresent(p -> {
                    p.setFaceRegistered(true);
                    personRepository.save(p);
                })
        );

        return ResponseEntity.ok("Faces registered: " + ids.size());
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> attendance(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        List<Integer> ids;

        if (user.getRole() == Role.TEACHER) {
            ids = faceEngineClient.scanStudents(user.getId());
        } else if (user.getRole() == Role.MANAGER) {
            ids = faceEngineClient.scanEmployees(user.getId());
        } else {
            return ResponseEntity.status(403).body("Not allowed");
        }

        return ResponseEntity.ok(ids);
    }
}
