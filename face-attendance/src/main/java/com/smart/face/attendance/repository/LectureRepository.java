package com.smart.face.attendance.repository;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture,Long> {

    List<Lecture> findByTeacher(User teacher);
}
