package com.smart.face.attendance.repository;

import com.smart.face.attendance.entity.EmployeeAttendance;
import com.smart.face.attendance.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeAttendanceRepository
        extends JpaRepository<EmployeeAttendance, Long> {

    Optional<EmployeeAttendance>
    findByEmployeeAndDate(Person employee, LocalDate date);
}
