package marowak.dev.service.character;

import keys.CharactersGetMessageKey;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

public interface CharacterService {
    void sendCharactersUpdate();

    void initCharacters(Flux<CharacterMessage> requests);

    void sendCharacterState(String characterName, boolean isOnline);

    void sendInitCharacter(CharactersGetMessageKey key, String characterName);
}
