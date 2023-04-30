package marowak.dev.service.character;

import keys.CharacterMessageKey;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

public interface CharacterService {
    void sendCharactersUpdate();

    void initCharacters(Flux<CharacterMessage> requests);

    void sendCharacterState(String characterName, boolean isOnline);

    void sendInitCharacter(CharacterMessageKey key, String characterName);
}
