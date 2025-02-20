package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.board.BoardInfoRequest;
import com.animalfarm.animalfarm_back.controller.response.board.*;
import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.SawPeopleService;
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
    private final SawPeopleService sawPeopleService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    User user = null;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestPart("board") BoardInfoRequest boardInfoRequest,
            @RequestParam("image") MultipartFile image,
            HttpSession session) throws IOException {
        BoardAddResponse boardAddResponse = new BoardAddResponse();
        try {

            if (session.getAttribute("userId") == null) {
                user = userService.findUserById("1");
                if (user == null) {
                    userService.saveOrUpdateUser("1", "익명", "익명", "https://hkwon.s3.ap-northeast-2.amazonaws.com/va/jumeok.png");
                    user = userService.findUserById("1");
                }
            } else {
                user = userService.findUserById(session.getAttribute("userId").toString());
            }


            BoardDto boardDto = boardService.saveBoard(BoardDto.fromBoardAdd(boardInfoRequest, 0), image, "va/", user);


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
            boardAddResponse.setIsSuccess(loginOrNot(session));
            boardAddResponse.setIsSuccess(0);
            return ResponseEntity.ok(boardAddResponse);
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
            @RequestPart("board") BoardInfoRequest boardInfoRequest,
            @RequestParam("image") MultipartFile image,
            @RequestBody String imageUrl,
            HttpSession session) {
        try {
            BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
            boardCompleteResponse.setIsLogin(loginOrNot(session));

            int success = boardService.updateNewBoard(BoardDto.fromBoardUpdate(boardInfoRequest, 0), image, "va/", board_id, imageUrl);
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

        BoardCompleteResponse boardCompleteResponse = new BoardCompleteResponse();
        int loginOrNot = loginOrNot(session);
        try {
            boardCompleteResponse.setIsLogin(loginOrNot);
            Board board = boardService.findById(board_id);
            String userId = (String) session.getAttribute("userId");

            User user = userService.findUserById(userId);

            sawPeopleService.save(board, user);

            boardCompleteResponse.setIsSuccess(1);

            return ResponseEntity.ok().body(boardCompleteResponse);

        } catch (Exception e) {
            boardCompleteResponse.setIsLogin(loginOrNot);
            boardCompleteResponse.setIsSuccess(0);
            return ResponseEntity.ok().body(boardCompleteResponse);
        }
    }

    @GetMapping("saw/{board_id}")
    public ResponseEntity<SawPeopleResponse> showSawPeopleList(@PathVariable Long board_id, HttpSession session) {
        int loginOrNot = loginOrNot(session);
        SawPeopleResponse sawPeopleResponse = new SawPeopleResponse();

        try {
            sawPeopleResponse.setIsLogin(loginOrNot);

            Board board = boardService.findById(board_id);

            User user = userService.findUserById(board.getUser().getId());

            List<SawPeopleDto> sawPeopleDtoList = sawPeopleService.getSawPeopleList(board, user);

            sawPeopleResponse.setPeople(sawPeopleDtoList);
            sawPeopleResponse.setIsSuccess(1);
            return ResponseEntity.ok().body(sawPeopleResponse);
        } catch (Exception e) {
            sawPeopleResponse.setIsLogin(loginOrNot);
            sawPeopleResponse.setPeople(null);
            sawPeopleResponse.setIsSuccess(0);
            return ResponseEntity.ok().body(sawPeopleResponse);
        }
    }

}




