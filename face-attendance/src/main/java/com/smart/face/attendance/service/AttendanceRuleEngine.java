package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.LectureAttendance;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AttendanceRuleEngine {

    public LectureAttendance.Status calculateStatus(
            LocalDateTime lectureStart,
            LocalDateTime arrival
    ){
        long minutesLate= Duration.between(lectureStart , arrival).toMinutes();

        if (minutesLate <=5)
            return LectureAttendance.Status.PRESENT;

        if (minutesLate <=20)
            return LectureAttendance.Status.LATE;

        if (minutesLate <=40)
            return LectureAttendance.Status.HALF_DAY;

        return LectureAttendance.Status.ABSENT;
    }
}
