package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;


    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createLecture(
            @RequestParam String subject,
            @RequestParam String room,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){

        Lecture lecture = lectureService.createLecture(
                subject,
                room,
                userDetails.getUser()
        );

        return ResponseEntity.ok(lecture);
    }


    // ✅ START
    @PostMapping("/{id}/start")
    public ResponseEntity<?> startLecture(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){

        lectureService.startLecture(id, userDetails.getUser().getId());

        return ResponseEntity.ok("Lecture Started ✅");
    }


    // ✅ END
    @PostMapping("/{id}/end")
    public ResponseEntity<?> endLecture(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){

        lectureService.endLecture(id, userDetails.getUser().getId());

        return ResponseEntity.ok("Lecture Ended ✅");
    }
}