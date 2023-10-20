package marowak.dev.service.motion;

import marowak.dev.request.CharacterMotionRequest;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface CharacterMotionService {
    Mono<Void> updateMotion(CharacterMotionRequest request, String playerName);

}
