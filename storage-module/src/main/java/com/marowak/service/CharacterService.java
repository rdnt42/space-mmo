package com.marowak.service;

import com.marowak.entity.Character;
import com.marowak.request.CharacterMotionRequest;
import com.marowak.request.CharacterRequest;
import org.reactivestreams.Publisher;

public interface CharacterService {
    Publisher<Character> create(CharacterRequest request);

    void updateMotion(CharacterMotionRequest request);
}
