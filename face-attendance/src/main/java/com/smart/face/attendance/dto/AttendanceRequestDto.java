package com.smart.face.attendance.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceRequestDto {

    private Long lectureId;
    private List<Long> userIds;
}