package com.animalfarm.animalfarm_back.controller.response.user;

import com.animalfarm.animalfarm_back.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {

    private String userId;
    private String name;
    private String email;
    private String image;

    public static UserDetailResponse from(UserDto userDto) {
        return UserDetailResponse.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .image(userDto.getImage())
                .build();
    }
}
