package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image) throws IOException {
        try {
            System.out.println(boardAddRequest);
            BoardDto boardDto = boardService.saveBoard(BoardDto.from(boardAddRequest), image, "va/");
            System.out.println("boardDto = " + boardDto.getTitle());
            BoardAddResponse boardAddResponse = new BoardAddResponse();
            boardAddResponse.setIsLogin(0); //로그인 확인 함수 필요
            boardAddResponse.setIsSuccess(1);
            return ResponseEntity.ok().body(boardAddResponse);
        } catch (Exception e) {
            BoardAddResponse errorResponse = BoardAddResponse.builder()
                    .isLogin(1) // 로그인 확인 함수 필요
                    .isSuccess(0)
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
    }
}
