package marowak.dev.service.motion;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;
import message.CharacterMessage;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {

    // TODO all response to flux/mono
    void leavingPlayer(String playerName);

    Collection<PlayerMotion> getAllMotions();

    void addMotion(CharacterMessage character);

    PlayersMotionListResponse updateAndGetMotions(PlayerMotionRequest request, String playerName);

    PlayersMotionListResponse getMotions(String playerName);

    boolean isPlayerInit(String playerName);
}
