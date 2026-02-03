package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.LectureRepository;
import com.smart.face.attendance.service.LectureAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureRepository lectureRepo;
    private final LectureAttendanceService service;
    private final RestTemplate restTemplate;

    @PostMapping
    public Lecture createLecture(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody Lecture lecture
    ) {
        lecture.setTeacher(user.getUser());
        lecture.setDate(LocalDate.now());
        return lectureRepo.save(lecture);
    }

    @PostMapping("/{lectureId}/scan")
    public ResponseEntity<?> scanLecture(
            @PathVariable Long lectureId
    ) {
        Lecture lecture = lectureRepo.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        String url = "http://127.0.0.1:5001/admin/"
                + lecture.getTeacher().getId()
                + "/attendance";

        Map<String, Object> res =
                restTemplate.postForObject(url, null, Map.class);

        // ✅ CORRECT KEY + NULL SAFETY
        List<Integer> presentIds =
                Optional.ofNullable((List<Integer>) res.get("matched"))
                        .orElse(Collections.emptyList());

        service.markAttendance(lecture, presentIds);

        return ResponseEntity.ok(presentIds);
    }
}