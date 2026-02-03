package com.smart.face.attendance.service;
import com.smart.face.attendance.entity.Lecture;
import com.smart.face.attendance.entity.User;
import com.smart.face.attendance.repository.LectureAttendanceRepository;
import com.smart.face.attendance.repository.LectureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepo;

    // ✅ CREATE (inactive lecture)
    @Transactional
    public Lecture createLecture(
            String subject,
            String room,
            User teacher
    ){

        Lecture lecture = Lecture.builder()
                .subject(subject)
                .room(room)
                .teacher(teacher)
                .active(false)
                .build();

        return lectureRepo.save(lecture);
    }


    // ✅ START
    @Transactional
    public void startLecture(Long lectureId, Long teacherId){

        Lecture lecture = lectureRepo.findById(lectureId)
                .orElseThrow(() ->
                        new RuntimeException("Lecture not found"));

        if(!lecture.getTeacher().getId().equals(teacherId)){
            throw new RuntimeException("Only teacher can start lecture");
        }

        // prevent multiple active lectures
        lectureRepo.findByActiveTrue()
                .ifPresent(l -> {
                    throw new RuntimeException(
                            "Another lecture already running!"
                    );
                });

        lecture.setActive(true);
        lecture.setStartTime(LocalTime.now());

        lectureRepo.save(lecture);
    }


    // ✅ END
    @Transactional
    public void endLecture(Long lectureId, Long teacherId){

        Lecture lecture = lectureRepo.findById(lectureId)
                .orElseThrow(() ->
                        new RuntimeException("Lecture not found"));

        if(!lecture.getTeacher().getId().equals(teacherId)){
            throw new RuntimeException("Only teacher can end lecture");
        }

        lecture.setActive(false);
        lecture.setEndTime(LocalTime.now());

        lectureRepo.save(lecture);
    }


    // ✅ GET ACTIVE
    public Lecture getActiveLecture(){
        return lectureRepo.findByActiveTrue()
                .orElseThrow(() ->
                        new RuntimeException("No active lecture"));
    }
}