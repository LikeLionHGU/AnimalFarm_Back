package com.animalfarm.animalfarm_back.controller.response.board;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCardResponse {
    private int isLogin;
    private List<BoardDto> board;
}
