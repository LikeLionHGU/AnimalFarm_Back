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

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + UUID.randomUUID() + "_" + uploadFile.getName();
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        removeNewFile(uploadFile);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("임시 파일 삭제 성공");
        } else {
            System.out.println("임시 저장 파일 삭제 실패");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        // 여기 이슈 잡을 필요.
        File convertFile = File.createTempFile("temp", file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        return Optional.of(convertFile);
    }
}
