package com.animalfarm.animalfarm_back.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
        //여기서 throw 던짐
        return upload(uploadFile, dirName);
    }

    // S3에 파일 업로드
    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + UUID.randomUUID() + "_" + uploadFile.getName(); // 파일명 생성
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)); // S3 업로드 :: ACL 설정 해준 사람을 위해서 추가해 둠.
        removeNewFile(uploadFile); // 임시 파일 삭제
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 로컬 임시 파일 삭제
    // 임시 파일 만드는 이유 : MultipartFile(Spring의 파일 형식)은 바로 PutObjectRequest에 사용 불가!
    // 따라서 MultipartFile을 File로 변환하는 과정이 있는 것! (file을 쓰는 것이 전통적인 방식!)
    public void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("임시 파일 삭제 성공: " + targetFile.getName());
        } else {
            System.out.println("임시 파일 삭제 실패: " + targetFile.getName());
        }
    }

    // MultipartFile -> File 변환
    public Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = File.createTempFile("temp", file.getOriginalFilename()); // 임시 파일 생성
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        return Optional.of(convertFile);
    }
}
