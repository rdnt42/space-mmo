package marowak.dev.service.bullet;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.service.physic.WeaponService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class BulletMotionServiceImpl implements BulletMotionService {

    private final WeaponService weaponService;

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String playerName) {
        return weaponService.updateShooting(request, playerName);
    }
}
