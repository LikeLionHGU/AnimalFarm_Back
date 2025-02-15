package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.board.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardCategoryRequest;
import com.animalfarm.animalfarm_back.controller.response.board.BoardAddResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardCardResponse;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/lost")
public class BoardLostController {
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

    @GetMapping("/main")
    public ResponseEntity<BoardCardResponse> getMainBoard(HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try{
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getMainLostBoards();
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new BoardCardResponse(0, null));
        }

    }

    @GetMapping("/all/category/new")
    public ResponseEntity<BoardCardResponse> getAllLostByNewOrder(
            @RequestBody BoardCategoryRequest request,
            HttpSession session) {
            int category = request.getCategory();
            BoardCardResponse boardCardResponse = new BoardCardResponse();
            try {
                boardCardResponse.setIsLogin(loginOrNot(session));

                List<BoardDto> boardDtoList = boardService.getAll

            } catch (Exception e){
                System.out.println(e.getMessage());
                boardCardResponse.setBoard(null);
                return ResponseEntity.ok(boardCardResponse);
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
