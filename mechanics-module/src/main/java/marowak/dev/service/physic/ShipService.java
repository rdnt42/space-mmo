package marowak.dev.service.physic;

import marowak.dev.dto.ship.ShipCreateRequest;
import marowak.dev.dto.world.SpaceShipBody;
import reactor.core.publisher.Mono;

public interface ShipService {

    Mono<SpaceShipBody> createShip(ShipCreateRequest request);


    Mono<Void> removeShip(String characterName);


}
