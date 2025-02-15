package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.service.BoardLostService;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/lost")
public class BoardLostController {
    private final BoardLostService boardlostService;
    private final UserService userService;
    private final BoardService boardService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    User user = null;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session) throws IOException {
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                boardAddResponse.setIsLogin(0);
                boardAddResponse.setIsSuccess(0);
                return ResponseEntity.ok(boardAddResponse);
            }
            user = userService.findUserById(userId);

            BoardDto boardDto = boardService.saveBoard(BoardDto.fromBoardAdd(boardAddRequest, 1), image, "va/", user);
            System.out.println(boardDto.getTitle());
            if (boardDto == null) {
                boardAddResponse.setIsSuccess(0);
            } else {
                boardAddResponse.setIsSuccess(1);
            }
            return ResponseEntity.ok(boardAddResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardAddResponse.setIsSuccess(0);
            return ResponseEntity.ok(boardAddResponse);
        }
    }
}
