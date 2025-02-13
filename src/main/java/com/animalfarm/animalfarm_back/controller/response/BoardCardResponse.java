package com.animalfarm.animalfarm_back.controller.response;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCardResponse {

    private Long boardId;
    private String title;
    private int category;
    private String image;
    private int timeType;
    private LocalDateTime updateDate;

    public static BoardCardResponse from(BoardDto boardDto) {
        return BoardCardResponse.builder()
                .boardId(boardDto.getId())
                .title(boardDto.getTitle())
                .category(boardDto.getCategory())
                .image(boardDto.getImage())
                //.timeType()
                .updateDate(boardDto.getUpdateDate())
                .build();
    }
}
