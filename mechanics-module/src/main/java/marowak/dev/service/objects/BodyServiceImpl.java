package marowak.dev.service.objects;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.world.Bullet;
import marowak.dev.service.world.WorldService;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Singleton
public class BodyServiceImpl implements BodyService {
    private final WorldService worldService;

    @Override
    public Flux<Bullet> getBullets(String characterName) {
        return worldService.getBulletsInRange(characterName);
    }
}
