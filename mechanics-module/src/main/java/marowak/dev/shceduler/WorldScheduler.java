package marowak.dev.shceduler;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.physic.WorldService;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class WorldScheduler {
    private final WorldService worldService;

    @Scheduled(fixedRate = "16ms")
    public void updateWorld() {
        worldService.updateWorld();
        // TODO добавить интерфейс сalculable
        worldService.calculateObjects();
    }
}
