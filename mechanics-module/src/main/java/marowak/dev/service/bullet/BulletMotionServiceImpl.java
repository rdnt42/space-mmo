package marowak.dev.service.bullet;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.service.physic.ShipService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class BulletMotionServiceImpl implements BulletMotionService {

    private final ShipService shipService;

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String playerName) {
        return shipService.updateShooting(request, playerName);
    }
}
