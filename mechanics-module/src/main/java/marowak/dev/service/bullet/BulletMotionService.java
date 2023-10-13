package marowak.dev.service.bullet;

import marowak.dev.request.CharacterShootingRequest;
import reactor.core.publisher.Mono;

public interface BulletMotionService {
    Mono<Void> updateShooting(CharacterShootingRequest request, String playerName);
}