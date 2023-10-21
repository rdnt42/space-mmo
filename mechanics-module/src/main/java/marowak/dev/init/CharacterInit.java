package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.character.CharacterService;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharacterService characterService;

    @EventListener
    public void initData(StartupEvent startupEvent) {
        characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ALL, "")
                .subscribe();
    }
}
