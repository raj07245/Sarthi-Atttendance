package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.repository.PersonRepository;
import com.smart.face.attendance.service.EmployeeAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeAttendanceController {

    private final EmployeeAttendanceService service;
    private final PersonRepository personRepository;

    @PostMapping("/{id}/check-in")
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        Person emp = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        service.checkIn(emp);
        return ResponseEntity.ok(Map.of("message", "Checked In", "employeeId", emp.getId()));
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        Person emp = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        service.checkOut(emp);
        return ResponseEntity.ok(Map.of("message", "Checked Out", "employeeId", emp.getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEmployees() {
        List<Person> employees = service.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}