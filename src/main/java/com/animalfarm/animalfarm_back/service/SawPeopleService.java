package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.SawPeople;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
import com.animalfarm.animalfarm_back.repository.SawPeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SawPeopleService {

    private final SawPeopleRepository sawPeopleRepository;

    public SawPeopleDto save(Board board, User user) {
        List<SawPeople> sawPeopleList = sawPeopleRepository.findAllByBoard(board);
        int doesExist = 0;
        if (!sawPeopleList.isEmpty()) {
            for (SawPeople sawPeople : sawPeopleList) {
                if (Objects.equals(sawPeople.getUser().getId(), user.getId())) {
                    doesExist= 1;
                    break;
                }
            }
        }
        if (doesExist == 0) {
            SawPeople sawPeople = SawPeople.from(board, user);
            return SawPeopleDto.fromSawPeople(sawPeopleRepository.save(sawPeople));
        } else {
            return null;
        }
    }

    public List<SawPeopleDto> getSawPeopleList(Board board) {
        List<SawPeople> sawPeople = sawPeopleRepository.findAllByBoard(board);
        List<SawPeopleDto> sawPeopleDtoList = new ArrayList<>();
        for (SawPeople sawPerson : sawPeople) {
            SawPeopleDto sawPeopleDto = SawPeopleDto.fromSawPeople(sawPerson);
            sawPeopleDtoList.add(sawPeopleDto);
        }
        return sawPeopleDtoList;
    }

}
