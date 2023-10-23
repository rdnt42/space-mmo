package marowak.dev.service.physic;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.SpaceShipBody;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ShipServiceImpl implements ShipService {
    private final WorldService worldService;

    @Override
    public Mono<SpaceShipBody> addShip(CharacterMotion motion) {
        SpaceShipBody ship = FactoryUtils.createShip(motion);
        worldService.createBody(ship);

        return Mono.just(ship);
    }

    @Override
    public Mono<Void> removeShip(String characterName) {
        SpaceShipBody ship = worldService.getBody(SpaceShipBody.class, characterName);
        worldService.removeBody(ship);

        return Mono.empty();
    }
}
