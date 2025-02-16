package com.animalfarm.animalfarm_back.dto;

import com.animalfarm.animalfarm_back.controller.request.board.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardUpdateRequest;
import com.animalfarm.animalfarm_back.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private String phoneNum;
    private int category;
    private int isFound;
    private int isRead;

    private String userId;
    private String userName;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private int timeType;
    private String printDate;

    private List<CommentDto> comments ;



    public static BoardDto fromBoardUpdate(BoardUpdateRequest boardUpdateRequest, int boardType) {
        return BoardDto.builder()
                .title(boardUpdateRequest.getTitle())
                .category(boardUpdateRequest.getCategory())
                .content(boardUpdateRequest.getContent())
                .location(boardUpdateRequest.getLocation())
                .longitude(boardUpdateRequest.getLongitude())
                .latitude(boardUpdateRequest.getLatitude())
                .detailLocation(boardUpdateRequest.getDetailLocation())
                .phoneNum(boardUpdateRequest.getPhoneNum())
                .boardType(boardType)
                .isFound(0)
                .isRead(0)
                .build();

    }

    public static BoardDto fromBoardAdd(BoardAddRequest boardAddRequest, int boardType) {
        return BoardDto.builder()
                .title(boardAddRequest.getTitle())
                .content(boardAddRequest.getContent())
                .location(boardAddRequest.getLocation())
                .longitude(boardAddRequest.getLongitude())
                .latitude(boardAddRequest.getLatitude())
                .detailLocation(boardAddRequest.getDetailLocation())
                .boardType(boardType)
                .phoneNum(boardAddRequest.getPhoneNum())
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
                .phoneNum(board.getPhoneNum())
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

    public static BoardDto fromDetailTimeTypeAdded(Board board, int timeType, String printDate) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .content(board.getContent())
                .image(board.getImage())
                .location(board.getLocation())
                .longitude(board.getLongitude())
                .latitude(board.getLatitude())
                .detailLocation(board.getDetailLocation())
                .phoneNum(board.getPhoneNum())
                .timeType(timeType)
                .printDate(printDate)
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .userName(board.getUser().getName())
                .build();
    }



    public static BoardDto fromDetailTimeTypeAddedLost(Board board,String printDate) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .content(board.getContent())
                .image(board.getImage())
                .location(board.getLocation())
                .longitude(board.getLongitude())
                .latitude(board.getLatitude())
                .detailLocation(board.getDetailLocation())
                .phoneNum(board.getPhoneNum())
                .printDate(printDate)
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .userName(board.getUser().getName())
                .comments(board.getComments() != null ?
                        board.getComments().stream()
                                .map(CommentDto::fromEntity)
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .build();
    }
}
