package marowak.dev.service.objects;

import marowak.dev.response.BulletBodyInfo;
import reactor.core.publisher.Flux;

public interface BodyService {
    Flux<BulletBodyInfo> getBullets(String characterName);
}
