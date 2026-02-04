package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final RestTemplate restTemplate;
    private final PersonRepository personRepository;

    // ✅ Teacher scans multi-face lecture
    @PostMapping("/scan")
    public ResponseEntity<List<Integer>> scan(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User admin = userDetails.getUser();
        String url = "http://127.0.0.1:5001/admin/" + admin.getId() + "/attendance";

        Map<String, Object> res = restTemplate.postForObject(url, null, Map.class);

        // ✅ Use correct key from Python response
        List<Integer> ids = (List<Integer>) res.get("matched");

        return ResponseEntity.ok(ids);
    }
}