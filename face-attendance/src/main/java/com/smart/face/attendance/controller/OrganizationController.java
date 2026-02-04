package com.smart.face.attendance.controller;

import com.smart.face.attendance.dto.OrganizationRequest;
import com.smart.face.attendance.entity.Organization;
import com.smart.face.attendance.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping("/create")
    public ResponseEntity<Organization> createOrg(@Valid @RequestBody OrganizationRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrgs() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrg(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrg(@PathVariable Long id,
                                                  @Valid @RequestBody OrganizationRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrg(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Organization deleted successfully");
    }
}