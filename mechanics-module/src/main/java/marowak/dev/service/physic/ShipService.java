package marowak.dev.service.physic;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ShipService {

    Mono<Void> addShip(CharacterMotion motion);

    Mono<Void> updateShip(CharacterMotionRequest request, String characterName);

    Mono<Void> updateShooting(CharacterShootingRequest request, String characterName);

    Mono<Void> deleteShip(String characterName);

    Flux<BodyInfo> getShipsInRange(String characterName);

    Flux<BodyInfo> getAllShips();

    Mono<BodyInfo> getShip(String characterName);

}
