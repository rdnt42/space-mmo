package marowak.dev.service.motion;

import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface CharacterMotionService {

    Mono<Void> leavingPlayer(String playerName);

    Flux<BodyInfo> getAllMotions();

    void addMotion(CharacterMessage character);

    Mono<Void> updateMotion(CharacterMotionRequest request, String playerName);

    // TODO move to another service?
    Mono<Void> updateShooting(CharacterShootingRequest request, String playerName);

    Flux<BodyInfo> getCharactersInRange(String playerName);

    Mono<BodyInfo> getCharacter(String playerName);
}
