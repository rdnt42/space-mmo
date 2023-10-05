package marowak.dev.service.motion;

import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.motion.CharactersMotions;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface CharacterMotionService {

    // TODO all response to flux/mono
    void leavingPlayer(String playerName);

    Flux<CharacterMotion> getAllMotions();

    void addMotion(CharacterMessage character);

    void updateMotion(CharacterMotionRequest request, String playerName);

    // TODO move to another service?
    void updateShooting(CharacterShootingRequest request, String playerName);

    CharactersMotions getCharactersInRange(String playerName);

    CharacterMotion getCharacterMotion(String playerName);
}
