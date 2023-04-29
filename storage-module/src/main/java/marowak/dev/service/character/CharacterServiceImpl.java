package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.repository.CharacterR2Repository;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterR2Repository characterR2Repository;

    @Override
    public Mono<CharacterMessage> create(CharacterMessage message) {
        Character character = Character.builder()
                .accountName(message.getAccountName())
                .characterName(message.getCharacterName())
                .x(message.getX())
                .y(message.getY())
                .angle(message.getAngle())
                .online(false)
                .build();

        return Mono.from(characterR2Repository.save(character))
                .map(characterToMessage);
    }

    @Override
    public Mono<CharacterMessage> updateMotion(CharacterMessage message) {
        characterR2Repository.update(message.getCharacterName(), message.getX(), message.getY(), message.getAngle());

        return Mono.empty();
    }

    @Override
    public Flux<CharacterMessage> getAllOnline() {
        return characterR2Repository.findByOnline(true)
                .map(characterToMessage);
    }

    @Override
    public Mono<CharacterMessage> get(String characterName) {
        return Mono.from(characterR2Repository.findById(characterName))
                .map(characterToMessage);
    }

    @Override
    public Mono<CharacterMessage> updateState(CharacterMessage message) {
        characterR2Repository.update(message.getCharacterName(), message.isOnline());

        return Mono.empty();
    }

    private final Function<Character, CharacterMessage> characterToMessage = character -> CharacterMessage.builder()
            .characterName(character.characterName())
            .accountName(character.accountName())
            .experience(character.experience())
            .x(character.x())
            .y(character.y())
            .angle(character.angle())
            .build();
}
