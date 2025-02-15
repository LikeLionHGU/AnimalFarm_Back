package com.animalfarm.animalfarm_back.controller.response.board;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFoundDetailResponse {

    private int isLogin;
    private int isUser;
    private BoardDto board;

}
