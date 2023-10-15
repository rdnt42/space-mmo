package marowak.dev.shceduler;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.physic.Calculable;
import marowak.dev.service.physic.WorldService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class WorldScheduler {
    private final WorldService worldService;
    private final List<Calculable> services;

    @Scheduled(fixedRate = "16ms")
    public void updateWorld() {
        worldService.updateWorld();
        services.forEach(Calculable::calculate);
    }
}
