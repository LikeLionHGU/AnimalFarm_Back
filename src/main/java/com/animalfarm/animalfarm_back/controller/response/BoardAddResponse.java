package com.animalfarm.animalfarm_back.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAddResponse {
    private int isLogin;
    private int isSuccess;
}
