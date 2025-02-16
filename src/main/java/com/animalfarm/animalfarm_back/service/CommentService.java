package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.Comment;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.CommentDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
import com.animalfarm.animalfarm_back.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final S3UploadService s3UploadService;
    private final BoardRepository boardRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public CommentDto saveComment(CommentDto commentDto, MultipartFile image, String dirName, User newUser, Board newBoard) throws IOException {
        String commentImageUrl = "";
        if (!image.isEmpty()) {
            File uploadFile = s3UploadService.convert(image)
                    .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
            commentImageUrl = s3UploadService.upload(uploadFile, dirName);
        }

        Comment comment = Comment.from(commentDto, commentImageUrl, newUser, newBoard);
        commentRepository.save(comment);
        return CommentDto.from(comment, generateImageUrl(comment.getImage()));
    }

    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }
}
