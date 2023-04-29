package marowak.dev.service.character;

import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CharacterService {
    Mono<CharacterMessage> create(CharacterMessage message);

    Mono<CharacterMessage> updateMotion(CharacterMessage message);

    Flux<CharacterMessage> getAllOnline();

    Mono<CharacterMessage> get(String characterName);

    Mono<CharacterMessage> updateState(CharacterMessage message);
}
