package marowak.dev.service.character;

import keys.CharacterMessageKey;
import marowak.dev.api.request.CharacterMotionRequest;
import message.CharacterMessage;
import reactor.core.publisher.Mono;

public interface CharacterService {
    Mono<Void> sendCharactersUpdate();

    Mono<Void> initCharacter(CharacterMessage message);

    Mono<Void> leavingPlayer(String characterName);

    Mono<Void> sendCharacterState(String characterName, boolean isOnline);

    Mono<Void> sendInitCharacter(CharacterMessageKey key, String characterName);

    Mono<Void> updateCharacterMotion(CharacterMotionRequest request, String characterName);
}
