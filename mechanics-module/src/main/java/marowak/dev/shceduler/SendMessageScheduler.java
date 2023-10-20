package marowak.dev.shceduler;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.character.CharacterService;

@RequiredArgsConstructor
@Singleton
public class SendMessageScheduler {
    private final CharacterService characterService;

    @Scheduled(fixedDelay = "1s", initialDelay = "5s")
    void sendCharactersUpdate() {
        characterService.sendCharactersUpdate()
                .subscribe();
    }
}
