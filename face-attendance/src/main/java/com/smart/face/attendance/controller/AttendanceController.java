package com.smart.face.attendance.controller;

import com.smart.face.attendance.dto.AttendanceRequestDto;
import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.AttendanceRepository;
import com.smart.face.attendance.repository.LectureRepository;
import com.smart.face.attendance.repository.PersonRepository;
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

    private final RestTemplate restTemplate;
    private final PersonRepository personRepository;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final LectureRepository lectureRepository;
    private AttendanceRequestDto attendanceRequestDto;

    // ✅ Teacher scans multi-face lecture
    @PostMapping("/scan")
    public ResponseEntity<List<Integer>> scan(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User admin = userDetails.getUser();
        String url = "http://127.0.0.1:5001/admin/" + admin.getId() + "/attendance_students";

        Map<String, Object> res = restTemplate.postForObject(url, null, Map.class);

        // ✅ Use correct key from Python response
        List<Integer> ids = (List<Integer>) res.get("matched");

        return ResponseEntity.ok(ids);
    }

    // ✅ MARK ATTENDANCE (Python -> Spring)
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(
            @RequestBody AttendanceRequestDto request
    ) {

        attendanceService.markAttendance(
                request.getUserIds(),
                request.getLectureId()
        );

        return ResponseEntity.ok("Attendance marked");
    }

    // ✅ MY ATTENDANCE
    @GetMapping("/my")
    public List<Attendance> myAttendance(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return attendanceRepository
                .findByUser(userDetails.getUser());
    }


    // ✅ LECTURE ATTENDANCE
    @GetMapping("/lecture/{id}")
    public List<Attendance> lectureAttendance(
            @PathVariable Long id
    ){
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow();

        return attendanceRepository.findByLecture(lecture);
    }
}
