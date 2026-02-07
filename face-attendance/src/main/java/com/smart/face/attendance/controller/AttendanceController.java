package com.smart.face.attendance.controller;

import com.smart.face.attendance.dto.AttendanceRequestDto;
import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.exception.NotFoundException;
import com.smart.face.attendance.repository.AttendanceRepository;
import com.smart.face.attendance.repository.LectureRepository;
import com.smart.face.attendance.service.AttendanceQueryService;
import com.smart.face.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceQueryService queryService;
    private final LectureRepository lectureRepository;

    @PostMapping("/mark")
    public ResponseEntity<?> mark(@RequestBody AttendanceRequestDto req) {
        attendanceService.markAttendance(
                req.getUserIds(),
                req.getLectureId()
        );
        return ResponseEntity.ok("Attendance marked");
    }

    @GetMapping("/my")
    public List<Attendance> myAttendance(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return queryService.myAttendance(userDetails.getUser());
    }

    @GetMapping("/lecture/{id}")
    public List<Attendance> lectureAttendance(@PathVariable Long id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lecture not found"));
        return queryService.lectureAttendance(lecture);
    }
}
