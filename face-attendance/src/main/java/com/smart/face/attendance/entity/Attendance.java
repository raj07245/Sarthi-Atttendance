package com.smart.face.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private  Lecture lecture;

    @Column(nullable = false,columnDefinition = "DATETIME CURRENT_TIMESTAMP")
    private LocalDateTime markedAt=LocalDateTime.now();


}
