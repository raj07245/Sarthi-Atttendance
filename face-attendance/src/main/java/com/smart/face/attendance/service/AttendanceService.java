package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.LectureAttendance;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.exception.NotFoundException;
import com.smart.face.attendance.repository.AttendanceRepository;
import com.smart.face.attendance.repository.LectureRepository;
import com.smart.face.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final AttendanceRuleEngine ruleEngine;

    public void markAttendance(List<Long> userIds, Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException("Lecture not found"));

        for (Long uid : userIds) {

            User user = userRepository.findById(uid)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (attendanceRepository.existsByUserAndLecture(user, lecture))
                continue;

            LectureAttendance.Status status =
                    ruleEngine.calculateStatus(
                            lecture.getStartTime().atDate(lecture.getDate()),
                            LocalDateTime.now()
                    );

            Attendance att = Attendance.builder()
                    .user(user)
                    .lecture(lecture)
                    .status(status)
                    .markedAt(LocalDateTime.now())
                    .build();

            attendanceRepository.save(att);
        }
    }
}
