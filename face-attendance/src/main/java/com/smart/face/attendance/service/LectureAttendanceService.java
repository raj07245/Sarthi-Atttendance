package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.LectureAttendance;
import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.repository.LectureAttendanceRepository;
import com.smart.face.attendance.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LectureAttendanceService {

    private final PersonRepository personRepository;
    private final LectureAttendanceRepository lectureAttendanceRepository;

    public void markAttendance(
            Lecture lecture,
            List<Integer> presentIds
    ) {

        if (presentIds == null){
            presentIds= Collections.emptyList();
        }

        Set<Integer> presentSet=new HashSet<>(presentIds);

        List<Person> students =
                personRepository.findByCreatedBy(
                        lecture.getTeacher()
                );

        for (Person s : students) {

            // ✅ Check if already exists
            LectureAttendance la =
                    lectureAttendanceRepository
                            .findByLectureAndStudent(lecture, s)
                            .orElseGet(LectureAttendance::new);

            la.setLecture(lecture);
            la.setStudent(s);

            if (presentIds.contains(s.getId().intValue())) {
                la.setStatus(LectureAttendance.Status.PRESENT);
            } else {
                la.setStatus(LectureAttendance.Status.ABSENT);
            }

            lectureAttendanceRepository.save(la);
        }
    }
}