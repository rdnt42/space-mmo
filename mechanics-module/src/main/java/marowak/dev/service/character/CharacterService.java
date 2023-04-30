package marowak.dev.service.character;

import keys.CharacterMessageKey;
import message.CharacterMessage;

public interface CharacterService {
    void sendCharactersUpdate();

    void initCharacters(CharacterMessage message);

    void sendCharacterState(String characterName, boolean isOnline);

    void sendInitCharacter(CharacterMessageKey key, String characterName);
}
