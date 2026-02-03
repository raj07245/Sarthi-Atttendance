package com.smart.face.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.smart.face.attendance")
@EnableJpaRepositories("com.smart.face.attendance")
public class FaceAttendanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaceAttendanceApplication.class, args);
	}

}
