package marowak.dev.service.world;

import io.micronaut.scheduling.annotation.Async;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorldService {
    void updateWorld();

    @Async
    void calculateObjects();

    void addShip(CharacterMotion motion);

    void updateShooting(CharacterShootingRequest request, String characterName);

    void updateShip(CharacterMotionRequest request, String characterName);

    void deleteShip(String characterName);

    Flux<BodyInfo> getShipsInRange(String characterName);

    Flux<BodyInfo> getAllShips();

    Flux<BodyInfo> getBulletsInRange(String characterName);

    Mono<BodyInfo> getShip(String characterName);
}
