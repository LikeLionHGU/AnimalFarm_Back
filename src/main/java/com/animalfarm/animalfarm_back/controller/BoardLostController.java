package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.board.*;
import com.animalfarm.animalfarm_back.controller.response.board.BoardAddResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardCardResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardCompleteResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardDetailResponse;
import com.animalfarm.animalfarm_back.domain.Board;
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
import java.util.Objects;

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
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getMainBoards(1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new BoardCardResponse(0, null));
        }

    }

    @GetMapping("/all/category/new")
    public ResponseEntity<BoardCardResponse> getAllLostByNewOrder(
            @RequestParam int category,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardNew(category, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/all/category/old")
    public ResponseEntity<BoardCardResponse> getAllLostByOldOrder(
            @RequestParam int category,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardOld(category, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/all/category/search/new")
    public ResponseEntity<BoardCardResponse> getAllLostBySearchNewOrder(
            @RequestParam("category") int category,
            @RequestParam("search") String search,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardSearchNew(category, search, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/all/category/search/old")
    public ResponseEntity<BoardCardResponse> getAllLostBySearchOldOrder(
            @RequestParam("category") int category,
            @RequestParam("search") String search,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardSearchOld(category, search, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/mypage/main")
    public ResponseEntity<BoardCardResponse> getMypageLostMain(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getMyPageMainBoard(user, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/mypage/all/new")
    public ResponseEntity<BoardCardResponse> getMypageLostMainAllNewOrder(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getAllMyPageBoardNew(user, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/mypage/all/old")
    public ResponseEntity<BoardCardResponse> getMypageLostMainAllOldOrder(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();

        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getAllMyPageBoardOld(user, 1);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(
        @PathVariable Long board_id,
        HttpSession session) {
        BoardDetailResponse boardDetailResponse = new BoardDetailResponse();
        try {
            boardDetailResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");

            User user = userService.findUserById(userId);
            BoardDto board = boardService.getDetailLostBoard(board_id, user);
            if (board == null) {
                boardDetailResponse.setIsUser(0);
                boardDetailResponse.setBoard(null);
                return ResponseEntity.ok(boardDetailResponse);
            }

            if (Objects.equals(board.getUserId(), userId)) {
                boardDetailResponse.setIsUser(1);
            } else {
                boardDetailResponse.setIsUser(0);
            }
            boardDetailResponse.setBoard(board);

            return ResponseEntity.ok(boardDetailResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardDetailResponse.setBoard(null);
            return ResponseEntity.ok(boardDetailResponse);
        }
    }

    @PutMapping("/{board_id}")
    public ResponseEntity<BoardAddResponse> updateBoard(
            @PathVariable Long board_id,
            @RequestPart("board") BoardUpdateRequest boardUpdateRequest,
            @RequestParam("image") MultipartFile image,
            @RequestPart("url") UrlRequest urlRequest,
            HttpSession session){
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        try {
            boardAddResponse.setIsLogin(loginOrNot(session));
            int success = boardService.updateNewBoard(BoardDto.fromBoardUpdate(boardUpdateRequest, 1), image, "va/", board_id, urlRequest.getUrl());
            if (success == 1) {
                boardAddResponse.setIsSuccess(1);
            } else {
                boardAddResponse.setIsSuccess(0);
            }
            return ResponseEntity.ok(boardAddResponse);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            boardAddResponse.setIsSuccess(0);
            return ResponseEntity.ok(boardAddResponse);
        }
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<BoardAddResponse> deleteBoard(
            @PathVariable Long board_id,
            HttpSession session) {
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        try {
            boardAddResponse.setIsLogin(loginOrNot(session));
            int status = boardService.deleteById(board_id);
            boardAddResponse.setIsSuccess(status);

            return ResponseEntity.ok(boardAddResponse);
        } catch (Exception e) {
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

    @PutMapping("/complete/{board_id}")
    public ResponseEntity<BoardCompleteResponse> completeBoard(
            @PathVariable Long board_id,
            HttpSession session) {
        BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
        try {
            boardCompleteResponse.setIsLogin(loginOrNot(session));

            int success = boardService.updateIsFound(BoardDto.fromUpdateIsFound(), board_id);
            if (success == 1) {
                boardCompleteResponse.setIsSuccess(1);
            } else {
                boardCompleteResponse.setIsSuccess(0);
            }
            return ResponseEntity.ok(boardCompleteResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCompleteResponse.setIsSuccess(0);
            return ResponseEntity.ok(boardCompleteResponse);
        }
    }

}
