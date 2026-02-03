package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Person person){

        User admin=userDetails.getUser();
        person.setCreatedBy(admin);
        person.setFaceRegistered(false);
        personRepository.save(person);

        return ResponseEntity.ok(person);
    }

    @PostMapping("/{personId}/register-face")
    public ResponseEntity<?> registerFace(
            @PathVariable Long personId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User admin = userDetails.getUser();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        if (!person.getCreatedBy().getId().equals(admin.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        String url = "http://127.0.0.1:5001/admin/"
                + admin.getId() + "/register-face/"+personId;
        Map res = restTemplate.postForObject(url, null, Map.class);

        if (Boolean.TRUE.equals(res.get("success"))) {
            person.setFaceRegistered(true);
            personRepository.save(person);
            return ResponseEntity.ok("Face registered");
        }

        return ResponseEntity.badRequest().body("Face scan failed");
    }
}
