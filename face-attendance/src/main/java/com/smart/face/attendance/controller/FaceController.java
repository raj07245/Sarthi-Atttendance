package com.smart.face.attendance.controller;

import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.entity.UserDetailsImpl;
import com.smart.face.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/face")
@RequiredArgsConstructor
public class FaceController {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


    @PostMapping("/register")
    public ResponseEntity<?> registerFace(@AuthenticationPrincipal UserDetailsImpl userDetails){

        if (userDetails == null){
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user=userDetails.getUser();

        String url="http://127.0.0.1:5001/user/register-face/"+user.getId();
        Map res = restTemplate.postForObject(url ,null,Map.class);

        if ((boolean) res.get("success")){
            user.setFaceRegistered(true);
            userRepository.save(user);
            return ResponseEntity.ok("Face register");
        }

        return ResponseEntity.badRequest().body("face scan failed");
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null){
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = userDetails.getUser();

        if (!user.isFaceRegistered()) {
            return ResponseEntity.badRequest().body("Face not registered");
        }

        String url = "http://127.0.0.1:5001/user/attendance/" + user.getId();
        Map res = new RestTemplate().postForObject(url, null, Map.class);

        if (res != null && Boolean.TRUE.equals(res.get("matched"))){
            return ResponseEntity.ok("Attendance marked");
        }

        return ResponseEntity.status(401).body("Face not matched");
    }
}
