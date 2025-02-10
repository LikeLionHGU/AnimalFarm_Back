package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    public BoardAddResponse saveBoard(BoardAddRequest boardAddRequest, String uploadUrl) {
        BoardDto boardDto = BoardDto.from(boardRepository.save(Board.from(boardAddRequest, uploadUrl)));
    }
}
