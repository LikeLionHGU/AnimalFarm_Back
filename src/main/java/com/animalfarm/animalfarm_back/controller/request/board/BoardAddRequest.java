package com.animalfarm.animalfarm_back.controller.request.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardAddRequest {
    private String title;
    private int category;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private String phoneNum;
    private String content;

    public BoardAddRequest(String content, String phoneNum, String detailLocation, double latitude, double longitude, String location, int category, String title) {
        this.content = content;
        this.phoneNum = phoneNum;
        this.detailLocation = detailLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.category = category;
        this.title = title;
    }


}
