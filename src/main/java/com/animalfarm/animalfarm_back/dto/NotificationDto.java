package com.animalfarm.animalfarm_back.dto;

import com.animalfarm.animalfarm_back.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private String title;
    private int category;
    private String printDate;
    private Long boardId;

    public static NotificationDto from(Board board, String printDate) {
        return NotificationDto.builder()
                .title(board.getTitle())
                .category(board.getCategory())
                .printDate(printDate)
                .boardId(board.getId())
                .build();
    }
}
