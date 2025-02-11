package com.animalfarm.animalfarm_back.service;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.animalfarm.animalfarm_back.domain.Board;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;
    private String boardImageUrl = null;


    private String uploadFileToS3(File uploadFile){
        UUID uuid = UUID.randomUUID();
        String fileName = uploadFile.getName() + "_" + uuid;

        putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return fileName;
    }

    private void removeNewFile(File targetFile){
        if(targetFile.delete()){
            System.out.println("파일이 삭제되었습니다.");
        }else{
            System.out.println("파일이 삭제되지 못했습니다.");
        }
    }

    private String putS3(File uploadFile, String fileName){
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = File.createTempFile("temp", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        return Optional.of(convertFile);
    }

    public BoardDto saveBoard(BoardDto boardDto, MultipartFile image) throws IOException {
        System.out.println("Start saving");
        if (image != null && !image.isEmpty()) {
            System.out.println("got file");
            File uploadFile = convert(image)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
            boardImageUrl = uploadFileToS3(uploadFile);
            System.out.println("got url");
        }
        System.out.println("dto to board: if this pop up without got file and got url it is error");
        Board board = Board.from(boardDto, boardImageUrl);
        boardRepository.save(board);
        System.out.println("saved board");
        return BoardDto.from(board, generateImageUrl(board.getImage()));
    }

    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }
}
