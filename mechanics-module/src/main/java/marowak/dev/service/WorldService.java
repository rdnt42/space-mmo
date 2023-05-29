package marowak.dev.service;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorldService {
    void updateWorld();

    void addShip(CharacterMotion motion);

    void updateShip(CharacterMotionRequest request, String characterName);

    void deleteShip(String characterName);

    Flux<CharacterMotion> getShipsInRange(String characterName);

    Flux<CharacterMotion> getAllShips();

    Mono<CharacterMotion> getShip(String characterName);
}
