package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.character.CharactersClient;

import java.io.Serializable;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharactersClient charactersClient;

    @EventListener
    public void initCharacters(StartupEvent startupEvent) {
        charactersClient.getCharacters(Collections.singletonList(Serializable.class))
                .doOnError(e -> log.error("Characters init error, error: {}", e.getMessage()))
                .doOnSuccess(c -> log.info("Character init successful"))
                .subscribe();
    }
}
