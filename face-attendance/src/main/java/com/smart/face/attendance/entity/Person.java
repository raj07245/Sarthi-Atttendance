package com.smart.face.attendance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role; // "EMPLOYEE" or "STUDENT"

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    public User getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(User createdBy){
        this.createdBy=createdBy;
    }

    // Attendance status
    private Boolean checkedIn = false;

    // Optional: face registration flag
    private Boolean faceRegistered = false;

    // Constructors
    public Person() {}
    public Person(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getCheckedIn() { return checkedIn; }
    public void setCheckedIn(Boolean checkedIn) { this.checkedIn = checkedIn; }

    public Boolean getFaceRegistered() { return faceRegistered; }
    public void setFaceRegistered(Boolean faceRegistered) { this.faceRegistered = faceRegistered; }
}
