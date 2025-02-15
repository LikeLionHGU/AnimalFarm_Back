package com.animalfarm.animalfarm_back.controller.response.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCompleteResponse {

    private int isLogin;
    private int isSuccess;
}
