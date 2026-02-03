package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.repository.PersonRepository;
import com.smart.face.attendance.service.EmployeeAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeAttendanceController {

    private final EmployeeAttendanceService service;
    private final PersonRepository personRepository;

    @PostMapping("/{id}/check-in")
    public ResponseEntity<?> checkIn(@PathVariable Long id) {

        Person emp = personRepository.findById(id).orElseThrow();
        service.checkIn(emp);

        return ResponseEntity.ok("Checked In");
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<?> checkOut(@PathVariable Long id) {

        Person emp = personRepository.findById(id).orElseThrow();
        service.checkOut(emp);

        return ResponseEntity.ok("Checked Out");
    }
}