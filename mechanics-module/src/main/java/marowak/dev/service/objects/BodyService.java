package marowak.dev.service.objects;

import marowak.dev.dto.world.Bullet;
import reactor.core.publisher.Flux;

public interface BodyService {
    Flux<Bullet> getBullets(String characterName);
}
