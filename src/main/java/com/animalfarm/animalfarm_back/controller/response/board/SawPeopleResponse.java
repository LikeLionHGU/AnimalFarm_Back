package com.animalfarm.animalfarm_back.controller.response.board;

import com.animalfarm.animalfarm_back.domain.SawPeople;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SawPeopleResponse {

    private int isLogin;
    private List<SawPeopleDto> people;
    private int isSuccess;
}
