package com.marowak.service;

import com.marowak.entity.Character;
import com.marowak.repository.CharacterRepository;
import com.marowak.request.CharacterMotionRequest;
import com.marowak.request.CharacterRequest;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService{
    private final CharacterRepository characterRepository;

    @Override
    public Publisher<Character> create(CharacterRequest request) {
        Character character = Character.builder()
                .accountName(request.accountName())
                .characterName(request.characterName())
                .x(request.x())
                .y(request.y())
                .angle(request.angle())
                .build();

        return characterRepository.save(character);
    }

    @Override
    public void updateMotion(CharacterMotionRequest request) {
        characterRepository.update(request.characterName(), request.x(), request.y(), request.angle());
    }
}
