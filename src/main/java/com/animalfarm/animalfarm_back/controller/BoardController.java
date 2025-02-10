package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.controller.response.BoardAddResponse;
import com.animalfarm.animalfarm_back.service.BoardService;
import com.animalfarm.animalfarm_back.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<BoardAddResponse> addBoard(
            @RequestParam(value = "board", required = true) BoardAddRequest boardAddRequest,
            @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        try {

            @Value("${cloud.aws.s3.bucket}")
            private String bucket;

            private final S3UploadService s3UploadService;

            String uploadUrl = null;
            if (multipartFile != null && !multipartFile.isEmpty()) {
                S3UploadService S3UploadService;
                uploadUrl = S3UploadService.uploadFiles(multipartFile, "likelion/");
            }

            BoardAddResponse response = boardService.saveBoard(boardAddRequest, uploadUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BoardAddResponse errorResponse = BoardAddResponse.builder()
                    .isLogin(1) // 로그인 확인 함수 필요
                    .isSuccess(0)
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
    }
}
