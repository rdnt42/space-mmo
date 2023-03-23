package marowak.dev.service.motion;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void leavingPlayer(String playerName);

    Collection<PlayerMotion> getAllMotions();

    void addMotion(CharacterMotionRequest character);

    PlayersMotionListResponse updateAndGetMotions(PlayerMotionRequest request, String playerName);

    boolean isPlayerInit(String playerName);
}
