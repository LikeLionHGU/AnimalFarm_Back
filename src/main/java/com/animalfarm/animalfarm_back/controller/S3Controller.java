package com.animalfarm.animalfarm_back.controller;


import com.animalfarm.animalfarm_back.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// AWS, IAM 설정: https://velog.io/@mingsound21/SpringBoot-AWS-S3%EB%A1%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C 이거 기반으로 하면 됨!
// aws에서 버킷 정책 편집 이것도 필수!
// 밑의 코드를 기반으로 수정해서 작업해도 됨. (더 좋은 코드로 바꿔도 됨.)

@RequiredArgsConstructor
@RestController
public class S3Controller {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    private final S3UploadService s3UploadService;

    // 단일 파일 업로드 처리
    @PostMapping("/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("images") MultipartFile multipartFile) {
        try {
            String uploadUrl = s3UploadService.uploadFiles(multipartFile, "va/");
            return ResponseEntity.ok(uploadUrl);
        } catch (Exception e) {
            return ResponseEntity.ok("이미지 업로드에 실패했습니다: " + e.getMessage());
        }
    }

    // 다중 파일 업로드 처리
    @PostMapping("/images")
    public ResponseEntity<String> updateUserImages(@RequestParam("images") MultipartFile[] multipartFiles) {
        StringBuilder uploadUrls = new StringBuilder();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String uploadUrl = s3UploadService.uploadFiles(multipartFile, "likelion-study/");
                uploadUrls.append(uploadUrl).append("\n");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("이미지 업로드에 실패했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok(uploadUrls.toString());
    }
}