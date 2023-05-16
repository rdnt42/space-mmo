package marowak.dev.service.motion;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface CharacterMotionService {

    // TODO all response to flux/mono
    void leavingPlayer(String playerName);

    Collection<CharacterMotion> getAllMotions();

    void addMotion(CharacterMessage character);

    Mono<Void> updateMotion(CharacterMotionRequest request, String playerName);

    Flux<CharacterMotion> getCharactersInRange(String playerName);
}
