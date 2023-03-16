package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.CharactersGetMessageKey;
import marowak.dev.service.character.CharacterService;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharacterService characterService;

    @EventListener
    public void initCharacters(StartupEvent startupEvent) {
        characterService.sendInitCharacters(CharactersGetMessageKey.CHARACTERS_GET_ALL, null);
    }
}
