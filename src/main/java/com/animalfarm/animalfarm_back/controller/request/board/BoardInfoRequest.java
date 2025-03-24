package com.animalfarm.animalfarm_back.controller.request.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardInfoRequest {

    @Schema(description = "게시판 제목", example = "지현이의 에어팟을 잃어버렸어요~~")
    private String title;
    private int category;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private String phoneNum;
    private String content;
}
