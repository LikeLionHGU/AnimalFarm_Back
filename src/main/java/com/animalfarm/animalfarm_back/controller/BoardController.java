package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.S3UploadService;
import jakarta.validation.Valid;
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

    private final S3UploadService s3UploadService;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @ModelAttribute("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image) throws IOException {
        try {
            BoardDto boardDto = boardService.saveBoard(BoardDto.from(boardAddRequest), image);
            System.out.println("boardDto = " + boardDto.getTitle());
            System.out.println("get image finished...");
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
