package com.marowak.service;

import com.marowak.repository.CharacterRepository;
import com.marowak.request.CharacterMotionRequest;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService{
    private final CharacterRepository characterRepository;

    @Override
    public void updateMotion(CharacterMotionRequest request) {
        characterRepository.update(request.characterName(), request.x(), request.y(), request.angle());
    }
}
