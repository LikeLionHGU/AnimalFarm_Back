package com.animalfarm.animalfarm_back.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenRequest {

    private String googleIdToken;
}
