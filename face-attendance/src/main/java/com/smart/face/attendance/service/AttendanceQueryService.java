package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendanceRepository repository;

    public List<Attendance> myAttendance(User user) {
        return repository.findByUser(user);
    }

    public List<Attendance> lectureAttendance(Lecture lecture) {
        return repository.findByLecture(lecture);
    }
}
