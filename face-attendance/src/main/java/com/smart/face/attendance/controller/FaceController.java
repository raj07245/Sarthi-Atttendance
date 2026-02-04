package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.entity.Role;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.PersonRepository;
import com.smart.face.attendance.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;

    // ✅ Role-based face registration
    @PostMapping("/register")
    public ResponseEntity<?> registerFaces(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null) return ResponseEntity.status(401).body("Unauthorized");

        User user = userDetails.getUser();
        String url;

        // Determine Python endpoint based on role
        if(user.getRole() == Role.TEACHER){
            url = "http://127.0.0.1:5001/admin/" + user.getId() + "/register_face_students";
        } else if(user.getRole() == Role.MANAGER){
            url = "http://127.0.0.1:5001/admin/" + user.getId() + "/register_face_employees";
        } else {
            return ResponseEntity.status(403).body("Only TEACHER or MANAGER can register faces");
        }

        Map<String,Object> res = restTemplate.postForObject(url, null, Map.class);

        List<Integer> registeredIds = (List<Integer>) res.get("registered_ids");
        if(registeredIds != null && !registeredIds.isEmpty()){
            for(Integer pid : registeredIds){
                personRepository.findById(pid.longValue()).ifPresent(p -> {
                    p.setFaceRegistered(true);
                    personRepository.save(p);
                });
            }
            return ResponseEntity.ok(registeredIds.size() + " faces registered successfully");
        }

        return ResponseEntity.badRequest().body("Face registration failed");
    }

    // ✅ Role-based attendance marking
    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null) return ResponseEntity.status(401).body("Unauthorized");

        User user = userDetails.getUser();
        String url;

        if(user.getRole() == Role.TEACHER){
            url = "http://127.0.0.1:5001/admin/" + user.getId() + "/attendance_students";
        } else if(user.getRole() == Role.MANAGER){
            url = "http://127.0.0.1:5001/admin/" + user.getId() + "/attendance_employees";
        } else {
            return ResponseEntity.status(403).body("Only TEACHER or MANAGER can mark attendance");
        }

        Map<String,Object> res = restTemplate.postForObject(url, null, Map.class);
        List<Integer> matchedIds = (List<Integer>) res.get("matched");

        if(matchedIds != null && !matchedIds.isEmpty()){
            return ResponseEntity.ok(matchedIds.size() + " attendances marked successfully");
        }

        return ResponseEntity.status(400).body("No faces matched");
    }
}