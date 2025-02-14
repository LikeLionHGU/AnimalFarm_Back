package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.UserDto;
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
    User user = null;

    @PostMapping("/found/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session) throws IOException {
        try {

            String userId = (String) session.getAttribute("userId");

            if (session.getAttribute("userId") == null) {
                user = userService.findUserById("1");
                if (user == null) {
                    userService.saveOrUpdateUser("1","익명","익명","https://hkwon.s3.ap-northeast-2.amazonaws.com/va/profile.png");
                    user = userService.findUserById("1");
                }
            } else {
                user = userService.findUserById(session.getAttribute("userId").toString());
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
