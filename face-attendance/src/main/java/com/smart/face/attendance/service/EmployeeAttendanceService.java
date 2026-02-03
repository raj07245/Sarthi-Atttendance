package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.EmployeeAttendance;
import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.repository.EmployeeAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EmployeeAttendanceService {

    private final EmployeeAttendanceRepository repo;

    public void checkIn(Person emp) {

        EmployeeAttendance att =
                repo.findByEmployeeAndDate(
                        emp, LocalDate.now()
                ).orElse(new EmployeeAttendance());

        att.setEmployee(emp);
        att.setDate(LocalDate.now());
        att.setCheckIn(LocalTime.now());

        if (att.getCheckIn().isAfter(LocalTime.of(9, 30))) {
            att.setStatus(EmployeeAttendance.Status.HALF_DAY);
        } else if (att.getCheckIn().isAfter(LocalTime.of(9, 0))) {
            att.setStatus(EmployeeAttendance.Status.LATE);
        } else {
            att.setStatus(EmployeeAttendance.Status.PRESENT);
        }

        repo.save(att);
    }

    public void checkOut(Person emp) {

        EmployeeAttendance att =
                repo.findByEmployeeAndDate(
                        emp, LocalDate.now()
                ).orElseThrow();

        att.setCheckOut(LocalTime.now());

        Duration d = Duration.between(
                att.getCheckIn(),
                att.getCheckOut()
        );

        if (d.toHours() < 4) {
            att.setStatus(EmployeeAttendance.Status.HALF_DAY);
        }

        repo.save(att);
    }
}
