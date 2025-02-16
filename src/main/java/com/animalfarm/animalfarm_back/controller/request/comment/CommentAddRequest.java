package com.animalfarm.animalfarm_back.controller.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddRequest {
    private String content;
}