package com.smart.face.attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    private String email;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "organization",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users; // Users in this org
}