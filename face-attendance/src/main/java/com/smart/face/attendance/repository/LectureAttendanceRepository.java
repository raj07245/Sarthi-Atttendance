package com.smart.face.attendance.repository;


import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.LectureAttendance;
import com.smart.face.attendance.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface LectureAttendanceRepository extends JpaRepository<LectureAttendance,Long> {

    Optional<LectureAttendance> findByLectureIdAndStudent(
            Lecture lecture,
            Person student
    );

    Optional<LectureAttendance> findByLectureAndStudent(Lecture lecture, Person s);
}
