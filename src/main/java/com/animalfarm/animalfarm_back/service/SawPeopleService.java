package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.SawPeople;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
import com.animalfarm.animalfarm_back.repository.SawPeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SawPeopleService {

    private final SawPeopleRepository sawPeopleRepository;

    public SawPeopleDto save(Board board, User user) {
        SawPeople sawPeople = SawPeople.from(board, user);
        SawPeopleDto sawPeopleDto = SawPeopleDto.fromSawPeople(sawPeopleRepository.save(sawPeople));
        return sawPeopleDto;
    }

    public List<SawPeopleDto> getSawPeopleList(Board board, User user) {
        List<SawPeople> sawPeople = sawPeopleRepository.findAllByBoardAndUser(board, user);
        List<SawPeopleDto> sawPeopleDtoList = null;
        for (SawPeople sawPerson : sawPeople) {
            SawPeopleDto sawPeopleDto = SawPeopleDto.fromSawPeople(sawPerson);
            sawPeopleDtoList.add(sawPeopleDto);
        }
        return sawPeopleDtoList;
    }

}
