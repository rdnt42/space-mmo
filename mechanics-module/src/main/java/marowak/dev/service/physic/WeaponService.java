package marowak.dev.service.physic;

import marowak.dev.dto.world.BulletBody;
import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeaponService {
    Flux<BodyInfo> getBulletsInRange(String characterName);

    Mono<Void> createBullet(BulletBody bullet);
}
