package com.marowak.service;

import com.marowak.entity.Character;
import com.marowak.request.CharacterRequest;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@KafkaListener
public class CharacterListener {
    private final CharacterService characterService;

    @Topic("characters")
    public Publisher<Character> createCharacter(CharacterRequest request) {
        return characterService.create(request);
    }
}
