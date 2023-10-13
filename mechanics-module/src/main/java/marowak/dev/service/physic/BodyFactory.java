package marowak.dev.service.physic;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.BodyCreator;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.motion.CharacterMotion;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class BodyFactory {
    private final ShipService shipService;
    private final WeaponService weaponService;

    public Mono<Void> create(BodyCreator createData) {
        return switch (createData) {
            case CharacterMotion data -> shipService.addShip(data);
            case BulletCreateRequest data -> weaponService.createBullet(data);
            default -> throw new IllegalStateException("Unexpected value: " + createData);
        };
    }
}
