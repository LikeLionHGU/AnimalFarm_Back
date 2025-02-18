package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.board.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardCategoryRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardSearchRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardUpdateRequest;
import com.animalfarm.animalfarm_back.controller.response.board.BoardAddResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardCardResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardCompleteResponse;
import com.animalfarm.animalfarm_back.controller.response.board.BoardDetailResponse;
import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
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
@RequestMapping("/board/found")
public class BoardFoundController {
    private final BoardService boardService;
    private final UserService userService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    User user = null;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardAddRequest boardAddRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session) throws IOException {
        try {

            if (session.getAttribute("userId") == null) {
                user = userService.findUserById("1");
                if (user == null) {
                    userService.saveOrUpdateUser("1", "익명", "익명", "https://hkwon.s3.ap-northeast-2.amazonaws.com/va/profile.png");
                    user = userService.findUserById("1");
                }
            } else {
                user = userService.findUserById(session.getAttribute("userId").toString());
            }


            BoardDto boardDto = boardService.saveBoard(BoardDto.fromBoardAdd(boardAddRequest, 0), image, "va/", user);

            BoardAddResponse boardAddResponse = new BoardAddResponse();
            if (session.getAttribute("userId") == null) {
                boardAddResponse.setIsLogin(0); //로그인 확인 함수 필요
            } else {
                boardAddResponse.setIsLogin(1);
            }
            if (boardDto == null) {
                boardAddResponse.setIsSuccess(0);
            } else {
                boardAddResponse.setIsSuccess(1);
            }

            return ResponseEntity.ok().body(boardAddResponse);
        } catch (Exception e) {
            //////////////////////////////////////////////////////////  확인 필요
            BoardAddResponse errorResponse = BoardAddResponse.builder()
                    .isLogin(1) // 로그인 확인 함수 필요
                    .isSuccess(0)
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
    }

    @GetMapping("/main")
    public ResponseEntity<BoardCardResponse> showMainBoard(HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getMainBoards(0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new BoardCardResponse(0, null));
        }

    }

    @GetMapping("/all/category/new")
    public ResponseEntity<BoardCardResponse> showAllCategoryBoardNew(
            @RequestParam int category,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardNew(category, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new BoardCardResponse(0, null));
        }
    }

    @GetMapping("/all/category/old")
    public ResponseEntity<BoardCardResponse> showCategoryBoardOld(
            @RequestParam int category,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardOld(category, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/all/category/search/new")
    public ResponseEntity<BoardCardResponse> showAllSearchBoardNew(
            @RequestParam("category") int category,
            @RequestParam("search") String search,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardSearchNew(category, search, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/all/category/search/old")
    public ResponseEntity<BoardCardResponse> showAllSearchBoardOld(
            @RequestParam("category") int category,
            @RequestParam("search") String search,
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));

            List<BoardDto> boardDtoList = boardService.getAllCategoryBoardSearchOld(category, search, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/mypage/main")
    public ResponseEntity<BoardCardResponse> showMyPageMainBoard(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getMyPageMainBoard(user,0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/mypage/all/new")
    public ResponseEntity<BoardCardResponse> showAllMyPageBoardNew(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getAllMyPageBoardNew(user, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }
    @GetMapping("/mypage/all/old")
    public ResponseEntity<BoardCardResponse> showAllMyPageBoardOld(
            HttpSession session) {
        BoardCardResponse boardCardResponse = new BoardCardResponse();
        try {
            boardCardResponse.setIsLogin(loginOrNot(session));
            String userId = (String) session.getAttribute("userId");
            User user = userService.findUserById(userId);

            List<BoardDto> boardDtoList = boardService.getAllMyPageBoardOld(user, 0);
            boardCardResponse.setBoard(boardDtoList);

            return ResponseEntity.ok(boardCardResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            boardCardResponse.setBoard(null);

            return ResponseEntity.ok(boardCardResponse);
        }
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<BoardDetailResponse> showDetailFoundBoard(@PathVariable Long board_id, HttpSession session) {
        try {
            BoardDetailResponse boardFoundDetailResponse = new BoardDetailResponse();
            boardFoundDetailResponse.setIsLogin(loginOrNot(session));

            String userId = (String) session.getAttribute("userId");

            BoardDto boardDto = boardService.getDetailFoundBoard(board_id);

            if (Objects.equals(boardDto.getUserId(), userId)){
                boardFoundDetailResponse.setIsUser(1);
            } else {
                boardFoundDetailResponse.setIsUser(0);
            }

            boardFoundDetailResponse.setBoard(boardDto);
            return ResponseEntity.ok().body(boardFoundDetailResponse);
        } catch (Exception e) {
            BoardDetailResponse boardFoundDetailResponse = new BoardDetailResponse();
            boardFoundDetailResponse.setIsLogin(0);
            boardFoundDetailResponse.setIsUser(0);
            boardFoundDetailResponse.setBoard(null);
            return ResponseEntity.ok().body(boardFoundDetailResponse);
        }
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<BoardCompleteResponse> deleteBoard(@PathVariable Long board_id, HttpSession session) {
        try {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(loginOrNot(session));

            int status = boardService.deleteById(board_id);

            boardCompleteResponse.setIsSuccess(status);

            return ResponseEntity.ok().body(boardCompleteResponse);
        } catch (Exception e) {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(0);
            boardCompleteResponse.setIsSuccess(0);
            return ResponseEntity.ok().body(boardCompleteResponse);
        }
    }

    @PutMapping("/{board_id}")
    public ResponseEntity<BoardCompleteResponse> updateBoard(
            @PathVariable Long board_id,
            @RequestPart("board") BoardUpdateRequest boardUpdateRequest,
            @RequestParam("image") MultipartFile image,
            @RequestBody String imageUrl,
            HttpSession session) {
        try {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(loginOrNot(session));

            int success = boardService.updateNewBoard(BoardDto.fromBoardUpdate(boardUpdateRequest, 0), image, "va/", board_id, imageUrl);
            boardCompleteResponse.setIsSuccess(success);
            return ResponseEntity.ok().body(boardCompleteResponse);
        } catch (Exception e) {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(0);
            boardCompleteResponse.setIsSuccess(0);
            return ResponseEntity.ok().body(boardCompleteResponse);
        }

    }

    private int loginOrNot(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return 0;
        }
        return 1;
    }

    @PostMapping("saw/{board_id}")
    public ResponseEntity<BoardCompleteResponse> sawBoard(@PathVariable Long board_id, HttpSession session) {
        try {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(loginOrNot(session));

            Board board = boardService.findById(board_id);

            User user = userService.findUserById(board.getUser().getId());



        } catch (Exception e) {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(0);
            boardCompleteResponse.setIsSuccess(0);
            return ResponseEntity.ok().body(boardCompleteResponse);
        }
    }

}




