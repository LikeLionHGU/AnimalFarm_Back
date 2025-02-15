package com.animalfarm.animalfarm_back.controller.response;

import lombok.*;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardAddResponse {
    private int isLogin;
    private int isSuccess;
}
