package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.BulletBodyView;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class WeaponServiceImpl implements WeaponService, Calculable {
    private final WorldService worldService;

    @Override
    public Flux<BulletBodyView> getBulletsInRange(String characterName) {
        return Mono.just(worldService.getBody(SpaceShipBody.class, characterName))
                .map(ship -> ship.getTransform().getTranslation())
                .flatMapMany(base -> Flux.fromStream(worldService.getBodies(BulletBody.class).stream())
                        .filter(body -> Utils.isInRange(base, body.getTransform().getTranslation()))
                        .map(Utils.bulletToBodyInfo));
    }

    @Override
    public Mono<Void> createBullet(BulletBody bullet) {
        worldService.createBody(bullet);

        return Mono.empty();
    }

    @Async
    @Override
    public void calculate() {
        worldService.getBodies(BulletBody.class)
                .forEach(this::calculateBullet);
    }

    private void calculateBullet(BulletBody body) {
        calculateLifeCycle(body);
    }

    private void calculateLifeCycle(BulletBody body) {
        if (body.isAtRest() || !body.isEnabled()) {
            worldService.removeBody(body);
        }
    }
}
