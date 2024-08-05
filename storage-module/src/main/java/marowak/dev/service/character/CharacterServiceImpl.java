package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Character;
import marowak.dev.repository.CharacterR2Repository;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterR2Repository characterR2Repository;

    @Override
    public Mono<CharacterMessage> create(CharacterMessage message) {
        Character res = Character.builder()
                .accountName(message.getAccountName())
                .characterName(message.getCharacterName())
                .x(message.getX())
                .y(message.getY())
                .angle((short) message.getAngle())
                .online(false)
                .build();

        return Mono.from(characterR2Repository.save(res))
                .map(character -> characterToMessage.apply(character, CharacterMessageKey.CHARACTER_CREATE));
    }

    @Override
    public Mono<CharacterMessage> updateMotion(CharacterMessage message) {
        characterR2Repository.update(message.getCharacterName(), message.getX(), message.getY(), message.getAngle());

        return Mono.just(message);
    }

    @Override
    public Flux<CharacterMessage> getAllOnline() {
        return characterR2Repository.findByOnline(true)
                .doOnNext(character -> log.info("Getting character: {}", character.characterName()))
                .map(character -> characterToMessage.apply(character, CharacterMessageKey.CHARACTERS_GET_ALL));
    }

    @Override
    public Mono<CharacterMessage> get(String characterName) {
        return Mono.from(characterR2Repository.findById(characterName))
                .doOnNext(character -> log.info("Getting character: {}", character.characterName()))
                .map(character -> characterToMessage.apply(character, CharacterMessageKey.CHARACTERS_GET_ONE));
    }

    @Override
    public Mono<CharacterMessage> updateState(CharacterMessage message) {
        characterR2Repository.update(message.getCharacterName(), message.isOnline());

        return Mono.just(message);
    }

    private final BiFunction<Character, CharacterMessageKey, CharacterMessage> characterToMessage =
            (character, key) -> CharacterMessage.builder()
                    .key(key)
                    .characterName(character.characterName())
                    .accountName(character.accountName())
                    .experience(character.experience())
                    .x(character.x())
                    .y(character.y())
                    .angle(character.angle())
                    .build();
}
