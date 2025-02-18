package com.animalfarm.animalfarm_back.dto;


import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SawPeopleDto {

    private Long boardId;
    private String userName;
    private LocalDateTime regDate;

    public static SawPeopleDto from(Board board, User user) {
        return SawPeopleDto.builder()
                .boardId(board.getId())
                .userName(user.getName())
                .build();
    }

    public static SawPeopleDto fromSawPeopleAdd(Board board, User user) {
    }
}
