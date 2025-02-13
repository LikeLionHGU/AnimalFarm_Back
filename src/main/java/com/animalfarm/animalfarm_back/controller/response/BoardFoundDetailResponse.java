package com.animalfarm.animalfarm_back.controller.response;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFoundDetailResponse {

    private Long boardId;
    private String title;
    private int category;
    private String content;
    private String image;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private String phoneNum;
    private int timeType;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private String userName;

    public static BoardFoundDetailResponse fromBoard(BoardDto boardDto, UserDto userDto) {
        return BoardFoundDetailResponse.builder()
                .boardId(boardDto.getId())
                .title(boardDto.getTitle())
                .category(boardDto.getCategory())
                .content(boardDto.getContent())
                .image(boardDto.getImage())
                .location(boardDto.getLocation())
                .longitude(boardDto.getLongitude())
                .latitude(boardDto.getLatitude())
                .detailLocation(boardDto.getDetailLocation())
                .phoneNum(boardDto.getPhoneNumber())
                //.timeType()
                .regDate(boardDto.getRegDate())
                .updateDate(boardDto.getUpdateDate())
                .userName(userDto.getName())
                .build();
    }

}
