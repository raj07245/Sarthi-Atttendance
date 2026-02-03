package com.smart.face.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "lecture_attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lecture_student",
                        columnNames = {"lecture_id","student_id"}
                )
        },
        indexes = {
                @Index(name="idx_lecture", columnList = "lecture_id"),
                @Index(name="idx_student", columnList = "student_id")
        }
)
public class LectureAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 ALWAYS LAZY in ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Person student;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status;

    // ⭐ VERY IMPORTANT
    @Column(nullable = false)
    private LocalDateTime markedAt;

    public enum Status{
        PRESENT,
        LATE,
        HALF_DAY,
        ABSENT
    }
}

