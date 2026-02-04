package com.smart.face.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private  Lecture lecture;

    @Column(name = "market_at",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime markedAt;


}
