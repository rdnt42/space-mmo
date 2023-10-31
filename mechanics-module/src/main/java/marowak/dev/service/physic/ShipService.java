package marowak.dev.service.physic;

import marowak.dev.dto.world.SpaceShipBody;
import reactor.core.publisher.Mono;

public interface ShipService {

    Mono<SpaceShipBody> createShip(SpaceShipBody body);


    Mono<Void> removeShip(String characterName);


}
