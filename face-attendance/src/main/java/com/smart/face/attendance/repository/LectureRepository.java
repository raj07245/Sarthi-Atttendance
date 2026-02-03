package com.smart.face.attendance.repository;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {

    Optional<Lecture> findByActiveTrue();
}
