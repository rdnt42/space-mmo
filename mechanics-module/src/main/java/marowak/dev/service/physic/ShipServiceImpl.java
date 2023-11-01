package marowak.dev.service.physic;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ShipServiceImpl implements ShipService {
    private final WorldService worldService;

    @Override
    public Mono<SpaceShipBody> createShip(SpaceShipBody body) {
        worldService.createBody(body);

        return Mono.just(body);
    }

    @Override
    public Mono<Void> removeShip(String characterName) {
        SpaceShipBody ship = worldService.getBody(SpaceShipBody.class, characterName);
        worldService.removeBody(ship);

        return Mono.empty();
    }

    @Override
    public Mono<Void> removeShipBodies(List<Body> bodies) {
        worldService.removeBodies(bodies);

        return Mono.empty();
    }
}
