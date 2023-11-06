package marowak.dev.service.physic;

import marowak.dev.api.response.BulletBodyInfo;
import marowak.dev.dto.world.BulletBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeaponService {
    Flux<BulletBodyInfo> getBulletsInRange(String characterName);

    Mono<Void> createBullet(BulletBody bullet);
}
