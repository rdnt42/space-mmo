package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.repository.CharacterRepository;
import marowak.dev.request.CharacterMotionRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;

    @Override
    public Publisher<Character> create(CharacterMotionRequest request) {
        Character character = Character.builder()
                .accountName(request.accountName())
                .characterName(request.characterName())
                .x(request.x())
                .y(request.y())
                .angle(request.angle())
                .isOnline(false)
                .build();

        return characterRepository.save(character);
    }

    @Override
    public Publisher<Character> updateMotion(CharacterMotionRequest request) {
        characterRepository.update(request.characterName(), request.x(), request.y(), request.angle());

        return Mono.empty();
    }

    @Override
    public Publisher<Character> getAll() {
        return characterRepository.findAll();
    }
}
