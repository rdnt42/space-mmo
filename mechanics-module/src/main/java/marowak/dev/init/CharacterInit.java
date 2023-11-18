package marowak.dev.init;

import io.micronaut.context.event.ShutdownEvent;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.character.CharacterService;

@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharacterService characterService;

    @EventListener
    public void initData(StartupEvent startupEvent) {
        characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ALL, "")
                .subscribe();
    }

    @EventListener
    public void onShutdownEvent(ShutdownEvent event) {
        characterService.sendCharactersUpdate()
                .subscribe();
    }
}
