package marowak.dev.service.physic;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.SpaceShipBody;
import reactor.core.publisher.Mono;

public interface ShipService {

    Mono<SpaceShipBody> addShip(CharacterMotion motion);


    Mono<Void> removeShip(String characterName);


}
