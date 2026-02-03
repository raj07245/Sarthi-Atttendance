package com.smart.face.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"lecture_id","student_id"})
        }
)
@Getter @Setter
@NoArgsConstructor
public class LectureAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status{
        PRESENT,
        ABSENT,
        LATE
    }
}

