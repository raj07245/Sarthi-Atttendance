package com.smart.face.attendance.dto;

import java.util.List;

public class FaceAttendanceResponse {

    private List<Long> matched;

    public  List<Long> getMatched(){
        return matched;
    }

    public void setMatched(List<Long> matched){
        this.matched=matched;
    }
}
