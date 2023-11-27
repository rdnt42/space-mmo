package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class InitCharacterCmd implements CharacterCommand<Void> {
    private final CharacterService characterService;

    @Override
    public Mono<Void> execute(String characterName) {
        return characterService.sendCharacterState(characterName, true)
                .then(characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ONE, characterName));
    }
}
