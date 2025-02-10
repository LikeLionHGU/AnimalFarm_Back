package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardAddResponse saveBoard(BoardAddRequest boardAddRequest, String uploadUrl) {
        BoardDto boardDto = BoardDto.fromBoard(boardRepository.save(Board.from(boardAddRequest, uploadUrl)));
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        boardAddResponse.setIsLogin(0);
        if (boardDto == null) {
            boardAddResponse.setIsSuccess(0);
        } else {
            boardAddResponse.setIsSuccess(1);
        }
        return boardAddResponse;
    }
}
