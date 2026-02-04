package com.smart.face.attendance.service;

import com.smart.face.attendance.dto.OrganizationRequest;
import com.smart.face.attendance.entity.Organization;
import com.smart.face.attendance.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repo;

    // ✅ Create org
    public Organization create(OrganizationRequest req) {
        Organization org = Organization.builder()
                .name(req.getName())
                .type(req.getType())
                .email(req.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        return repo.save(org);
    }

    // ✅ Get all orgs
    public List<Organization> getAll() {
        return repo.findAll();
    }

    // ✅ Get org by ID
    public Organization getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    // ✅ Update org
    public Organization update(Long id, OrganizationRequest req) {
        Organization org = getById(id);
        org.setName(req.getName());
        org.setType(req.getType());
        org.setEmail(req.getEmail());
        return repo.save(org);
    }

    // ✅ Delete org
    public void delete(Long id) {
        Organization org = getById(id);
        repo.delete(org);
    }
}