package com.smart.face.attendance.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
