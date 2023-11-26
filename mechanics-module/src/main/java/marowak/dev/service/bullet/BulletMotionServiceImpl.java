package marowak.dev.service.bullet;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.service.character.CharacterShipService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class BulletMotionServiceImpl implements BulletMotionService {

    private final CharacterShipService characterShipService;

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        return characterShipService.updateShooting(request, characterName);
    }
}
