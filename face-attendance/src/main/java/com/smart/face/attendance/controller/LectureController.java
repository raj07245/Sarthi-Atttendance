package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/create")
    public ResponseEntity<Lecture> createLecture(
            @RequestParam String subject,
            @RequestParam String room,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Lecture lecture = new Lecture();
        lecture.setSubject(subject);
        lecture.setRoom(room);
        lecture.setTeacher(userDetails.getUser());
        lecture.setDate(LocalDate.now());
        lecture.setStartTime(LocalTime.now());
        lecture.setEndTime(LocalTime.now());
        return ResponseEntity.ok(lecture);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<String> startLecture(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        lectureService.startLecture(id, userDetails.getUser().getId());
        return ResponseEntity.ok("Lecture Started ✅");
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<String> endLecture(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        lectureService.endLecture(id, userDetails.getUser().getId());
        return ResponseEntity.ok("Lecture Ended ✅");
    }
}