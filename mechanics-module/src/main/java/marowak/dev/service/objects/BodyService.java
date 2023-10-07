package marowak.dev.service.objects;

import marowak.dev.response.BodyInfo;
import reactor.core.publisher.Flux;

public interface BodyService {
    Flux<BodyInfo> getBullets(String characterName);
}
