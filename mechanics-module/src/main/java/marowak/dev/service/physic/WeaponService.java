package marowak.dev.service.physic;

import marowak.dev.enums.BulletType;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeaponService {
    Mono<Void> updateShooting(CharacterShootingRequest request, String characterName);

    Flux<BodyInfo> getBulletsInRange(String characterName);

    Mono<Void> createBullet(double angle, double x, double y, BulletType type);
}
