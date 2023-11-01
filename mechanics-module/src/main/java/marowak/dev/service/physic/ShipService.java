package marowak.dev.service.physic;

import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ShipService {

    Mono<SpaceShipBody> createShip(SpaceShipBody body);


    Mono<Void> removeShip(String characterName);

    Mono<Void> removeShipBodies(List<Body> bodies);

}
