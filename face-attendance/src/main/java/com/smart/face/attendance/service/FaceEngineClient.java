package com.smart.face.attendance.service;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FaceEngineClient {

    private final RestTemplate restTemplate;

    @Value("${face.engine.base-url}")
    private String baseUrl;

    public List<Integer> scanStudents(Long adminId) {
        String url = baseUrl + "/admin/" + adminId + "/attendance_students";
        Map res = restTemplate.postForObject(url, null, Map.class);
        return (List<Integer>) res.get("matched");
    }

    public List<Integer> scanEmployees(Long adminId) {
        String url = baseUrl + "/admin/" + adminId + "/attendance_employees";
        Map res = restTemplate.postForObject(url, null, Map.class);
        return (List<Integer>) res.get("matched");
    }

    public List<Integer> registerStudents(Long adminId) {
        String url = baseUrl + "/admin/" + adminId + "/register_face_students";
        Map res = restTemplate.postForObject(url, null, Map.class);
        return (List<Integer>) res.get("registered_ids");
    }

    public List<Integer> registerEmployees(Long adminId) {
        String url = baseUrl + "/admin/" + adminId + "/register_face_employees";
        Map res = restTemplate.postForObject(url, null, Map.class);
        return (List<Integer>) res.get("registered_ids");
    }
}