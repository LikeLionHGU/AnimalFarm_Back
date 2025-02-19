package com.animalfarm.animalfarm_back.controller.request.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchRequest {
    private int category;
    private String search;
}
