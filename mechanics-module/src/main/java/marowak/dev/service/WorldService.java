package marowak.dev.service;

import io.micronaut.scheduling.annotation.Async;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.Bullet;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
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

    Flux<CharacterMotion> getShipsInRange(String characterName);

    Flux<CharacterMotion> getAllShips();

    Flux<Bullet> getBulletsInRange(String characterName);

    Mono<CharacterMotion> getShip(String characterName);
}
