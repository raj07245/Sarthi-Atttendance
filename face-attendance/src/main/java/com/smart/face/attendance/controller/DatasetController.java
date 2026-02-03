package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDataset(
            @RequestParam("images") MultipartFile image,
            Authentication authentication
    ) throws IOException {

        //  Logged-in user email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Project root path
        String projectRoot = System.getProperty("user.dir");

        //  user-specific folder
        String userFolderPath = projectRoot
                + File.separator
                + uploadDir
                + File.separator
                + "user_" + user.getId();

        File userFolder = new File(userFolderPath);

        //  VERY IMPORTANT
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }

        //  file name
        String fileName = UUID.randomUUID() + ".jpg";
        File destination = new File(userFolder, fileName);

        image.transferTo(destination);

        System.out.println("Image saved at: " + destination.getAbsolutePath());

        return ResponseEntity.ok("Image uploaded successfully");
    }
}
