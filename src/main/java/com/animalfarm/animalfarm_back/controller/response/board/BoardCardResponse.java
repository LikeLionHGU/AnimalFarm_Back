package com.animalfarm.animalfarm_back.controller.response;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCardResponse {
    private int isLogin;
    private List<BoardDto> board;
}
