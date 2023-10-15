package marowak.dev.service.physic;

import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;

public interface WeaponService {
    Flux<BodyInfo> getBulletsInRange(String characterName);
}
