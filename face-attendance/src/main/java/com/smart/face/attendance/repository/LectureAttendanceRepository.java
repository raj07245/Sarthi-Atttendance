package com.smart.face.attendance.repository;


import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.LectureAttendance;
import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LectureAttendanceRepository extends JpaRepository<LectureAttendance,Long> {

    Optional<LectureAttendance> findByLectureIdAndStudent(
            Lecture lecture,
            User user
    );

    Optional<LectureAttendance> findByLectureAndStudent(Lecture lecture, Person s);

    List<LectureAttendance> findByLecture(Lecture lecture);

    boolean existsByLectureIdAndUserId(Long id, Long aLong);

    Collection<Object> findByLectureId(Long id);
}
