package marowak.dev.service;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorldService {
    void updateWorld();

    void addShip(CharacterMotion motion);

    void updateShooting(CharacterShootingRequest request, String characterName);

    void updateShip(CharacterMotionRequest request, String characterName);

    void deleteShip(String characterName);

    Flux<CharacterMotion> getShipsInRange(String characterName);

    Flux<CharacterMotion> getAllShips();

    Mono<CharacterMotion> getShip(String characterName);
}
