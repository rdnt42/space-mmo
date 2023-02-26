package marowak.dev.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.character.CharacterService;

@RequiredArgsConstructor
@Singleton
public class SendMessageScheduler {
    private final CharacterService characterService;

    @Scheduled(fixedDelay = "100ms", initialDelay = "5s")
    void sendCharactersUpdate() {
        characterService.sendCharactersUpdate();
    }
}
