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
                .phoneNumber(board.getPhoneNumber())
                .category(board.getCategory())
                .isFound(board.getIsFound())
                .isRead(board.getIsRead())
                .userId(board.getUser().getId())
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .build();
    }

    public static BoardDto fromBoardAddRequest(BoardAddRequest boardAddRequest, Long boardId) {
        return BoardDto.builder()
                .id(boardId)
                .title(boardAddRequest.getTitle())
                .category(boardAddRequest.getCategory())
                .location(boardAddRequest.getLocation())
                .longitude(boardAddRequest.getLongitude())
                .latitude(boardAddRequest.getLatitude())
                .detailLocation(boardAddRequest.getDetailLocation())
                .phoneNumber(boardAddRequest.getPhoneNum())
                .content(boardAddRequest.getContent())
                .boardType(boardAddRequest.getBoardType())
                .build();
    }

}
