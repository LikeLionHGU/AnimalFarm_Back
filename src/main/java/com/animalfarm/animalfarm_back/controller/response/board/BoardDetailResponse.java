package com.animalfarm.animalfarm_back.controller.response.board;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailResponse {

    private int isLogin;
    private int isUser;
    @Schema(description = "보드")
    private BoardDto board;

}
