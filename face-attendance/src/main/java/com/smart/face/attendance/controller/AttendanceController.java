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

    @PostMapping("/scan")
    public ResponseEntity<?> scan(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User admin = userDetails.getUser();
        String url = "http://127.0.0.1:5001/admin/"
                + admin.getId()+"/attendance";

        Map res = restTemplate.postForObject(url, null, Map.class);
        System.out.println("RAW RESPONSE = "+ res);
        List<Integer> ids=(List<Integer>) res.get("presents_ids");

        return ResponseEntity.ok(ids);
    }
}
