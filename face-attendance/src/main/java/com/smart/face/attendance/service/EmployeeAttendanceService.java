package com.smart.face.attendance.service;

import com.smart.face.attendance.entity.EmployeeAttendance;
import com.smart.face.attendance.entity.Organization;
import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.repository.EmployeeAttendanceRepository;
import com.smart.face.attendance.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeAttendanceService {

    private final PersonRepository personRepository;

    public void checkIn(Person emp) {
        emp.setCheckedIn(true);
        personRepository.save(emp);
    }

    public void checkOut(Person emp) {
        emp.setCheckedIn(false);
        personRepository.save(emp);
    }

    public List<Person> getAllEmployees() {
        return personRepository.findAll();
    }
}
