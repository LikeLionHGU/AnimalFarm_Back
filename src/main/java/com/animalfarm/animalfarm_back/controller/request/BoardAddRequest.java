package com.animalfarm.animalfarm_back.controller.request;

import lombok.Data;

@Data
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

    public BoardAddRequest(int category, String title, String location, String phoneNum, String content) {
        this.category = category;
        this.title = title;
        this.location = location;
        this.phoneNum = phoneNum;
        this.content = content;
    }

    public BoardAddRequest(int category, String title, double longitude, double latitude, String detailLocation, String phoneNum, String content) {
        this.category = category;
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.detailLocation = detailLocation;
        this.phoneNum = phoneNum;
        this.content = content;
    }
}
