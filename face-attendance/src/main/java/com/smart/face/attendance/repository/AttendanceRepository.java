package com.smart.face.attendance.repository;


import com.smart.face.attendance.entity.Attendance;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance ,Long> {

    List<Attendance> findByUser(User user);

    List<Attendance> findByLecture(Lecture lecture);

    boolean existsByUserAndLecture(User user, Lecture lecture);
}
