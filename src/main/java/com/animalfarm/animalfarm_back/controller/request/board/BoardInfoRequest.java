package com.animalfarm.animalfarm_back.controller.request.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardInfoRequest {
    private String title;
    private int category;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private String phoneNum;
    private String content;
}
