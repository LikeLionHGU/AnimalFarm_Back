package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.comment.CommentAddRequest;
import com.animalfarm.animalfarm_back.controller.response.board.BoardAddResponse;
import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.CommentDto;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.CommentService;
import com.animalfarm.animalfarm_back.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;
    User user = null;
    Board board = null;

    @PostMapping("/add/{board_id}")
    public ResponseEntity<BoardAddResponse> addComment(
            @PathVariable("board_id") Long board_id,
            @RequestPart("content") CommentAddRequest commentAddRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session){
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        try {
            boardAddResponse.setIsLogin(loginOrNot(session));
            if (session.getAttribute("userId") == null) {
                user = userService.findUserById("1");
                if (user == null) {
                    userService.saveOrUpdateUser("1", "익명", "익명", "https://hkwon.s3.ap-northeast-2.amazonaws.com/va/jumeok.png");
                    user = userService.findUserById("1");
                }
            } else {
                user = userService.findUserById(session.getAttribute("userId").toString());
            }

            board = boardService.findBoardById(board_id);

            CommentDto commentDto;

            if (image == null) {
                commentDto = commentService.saveCommentWithoutImage(CommentDto.fromCommentAdd(commentAddRequest), user, board);
            }else {
                commentDto = commentService.saveComment(CommentDto.fromCommentAdd(commentAddRequest), image, "va/", user, board);
            }


            if (commentDto == null) {
                boardAddResponse.setIsSuccess(0);
            }
            else {
                boardAddResponse.setIsSuccess(1);
            }
            return ResponseEntity.ok(boardAddResponse);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            boardAddResponse.setIsSuccess(0);
            return ResponseEntity.ok(boardAddResponse);
        }

    }
    private int loginOrNot(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return 0;
        }
        return 1;
    }
}
