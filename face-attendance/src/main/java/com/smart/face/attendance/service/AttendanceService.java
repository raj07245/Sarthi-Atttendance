package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.LectureAttendance;
import com.smart.face.attendance.entity.User;
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
    private  final LectureRepository lectureRepository;

    public void markAttendance(List<Long> userIds,Long lectureId){
        Lecture lecture=lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not foud"));

        for (Long userId: userIds){
            User student = userRepository.findById(userId)
                    .orElseThrow(() ->new RuntimeException("User not found"));

            if (attendanceRepository
                    .existsByUserAndLecture(student , lecture)){
                continue;
            }

            Attendance attendance= Attendance.builder()
                    .user(student)
                    .lecture(lecture)
                    .markedAt(LocalDateTime.now())
                    .status(LectureAttendance.Status.PRESENT)
                    .build();

            attendanceRepository.save(attendance);
        }
    }
}
