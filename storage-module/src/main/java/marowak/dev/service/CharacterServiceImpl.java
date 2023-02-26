package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.repository.CharacterRepository;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
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
    public Publisher<Character> updateMotion(CharacterRequest request) {
        characterRepository.update(request.characterName(), request.x(), request.y(), request.angle());

        return Mono.empty();
    }
}
