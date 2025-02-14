package com.animalfarm.animalfarm_back.dto;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String image;
    private String content;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private int boardType;
    private String phoneNumber;
    private int category;
    private int isFound;
    private int isRead;

    private String userId;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private int timeType;
    private String printDate;

    public static BoardDto fromBoard(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .image(board.getImage())
                .content(board.getContent())
                .location(board.getLocation())
                .longitude(board.getLongitude())
                .latitude(board.getLatitude())
                .detailLocation(board.getDetailLocation())
                .boardType(board.getBoardType())
                .phoneNumber(board.getPhoneNum())
                .category(board.getCategory())
                .isFound(board.getIsFound())
                .isRead(board.getIsRead())
                .userId(board.getUser().getId())
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .build();
    }

    public static BoardDto fromBoardAddRequest(Board board) {
        return BoardDto.builder()
                .title(board.getTitle())
                .category(board.getCategory())
                .location(board.getLocation())
                .longitude(board.getLongitude())
                .latitude(board.getLatitude())
                .detailLocation(board.getDetailLocation())
                .phoneNumber(board.getPhoneNum())
                .content(board.getContent())
                .boardType(board.getBoardType())
                .build();
    }

    public static BoardDto fromFoundBoardAdd(BoardAddRequest boardAddRequest) {
        return BoardDto.builder()
                .title(boardAddRequest.getTitle())
                .content(boardAddRequest.getContent())
                .location(boardAddRequest.getLocation())
                .longitude(boardAddRequest.getLongitude())
                .latitude(boardAddRequest.getLatitude())
                .detailLocation(boardAddRequest.getDetailLocation())
                .boardType(0)
                .phoneNumber(boardAddRequest.getPhoneNum())
                .category(boardAddRequest.getCategory())
                .isFound(0)
                .isRead(0)
                .build();
    }

    public static BoardDto from(Board board, String url) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .location(board.getLocation())
                .longitude(board.getLongitude())
                .detailLocation(board.getDetailLocation())
                .boardType(board.getBoardType())
                .phoneNumber(board.getPhoneNum())
                .category(board.getCategory())
                .image(url)
                .isFound(board.getIsFound())
                .isRead(board.getIsRead())
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .build();
    }


    public static BoardDto fromTimeTypeAdded(Board board, int timeType, String printDate) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .image(board.getImage())
                .timeType(timeType)
                .printDate(printDate)
                .build();
    }
}
