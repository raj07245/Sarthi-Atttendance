package com.smart.face.attendance.repository;

import com.smart.face.attendance.entity.Person;
import com.smart.face.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface PersonRepository extends JpaRepository<Person,Long> {
    List<Person> findByCreatedBy(User user);

}
