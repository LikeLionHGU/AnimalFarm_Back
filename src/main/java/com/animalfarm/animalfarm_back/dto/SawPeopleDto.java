package com.animalfarm.animalfarm_back.dto;


import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.SawPeople;
import com.animalfarm.animalfarm_back.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SawPeopleDto {

    private String userName;

    public static SawPeopleDto fromSawPeople(SawPeople sawPeople) {
        return SawPeopleDto.builder()
                .userName(sawPeople.getUser().getName())
                .build();
    }

}
