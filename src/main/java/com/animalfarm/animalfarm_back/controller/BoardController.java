package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    User user;

    @PostMapping("/found/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session ) throws IOException {
        try {
            if (session == null) {
                user = userService.findUserById("1");
                if (user == null) {
                    File newImage = null;
                    //새로운 이미지 파일 처리 익명 유저 프로필
                    String newImageUrl = null;
                    user = new User();
                    user.setId("1");
                    user.setName("익명");
                    user.setEmail("익명");
                    user.setImage(newImageUrl);
                }
            } else {
                user = userService.findUserById((String) session.getAttribute("userId"));
            }

            BoardDto boardDto = boardService.saveBoard(BoardDto.from(boardAddRequest), image, "va/", user);

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
