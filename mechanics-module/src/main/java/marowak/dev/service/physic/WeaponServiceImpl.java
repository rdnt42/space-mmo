package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class WeaponServiceImpl implements WeaponService, Calculable {

    private final WorldService worldService;

    @Override
    public Flux<BodyInfo> getBulletsInRange(String characterName) {
        return Mono.just(worldService.getBody(SpaceShip.class, characterName))
                .map(ship -> ship.getTransform().getTranslation())
                .flatMapMany(base -> Flux.fromStream(worldService.getBodies(KineticBullet.class).stream())
                        .filter(body -> Utils.isInRange(base, body.getTransform().getTranslation()))
                        .map(Utils.bodyToBodyInfo));
    }

    @Async
    @Override
    public void calculate() {
        worldService.getBodies(KineticBullet.class)
                .forEach(this::calculateBullet);
    }

    private void calculateBullet(IdentifiablePhysicalBody body) {
        if (body.isAtRest()) {
            worldService.removeBody(body);
        }
    }
}
