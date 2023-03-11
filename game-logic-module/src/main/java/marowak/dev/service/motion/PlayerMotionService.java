package marowak.dev.service.motion;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.PlayerMotionRequest;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void updatePlayerMotion(String playerName, PlayerMotionRequest request);

    Collection<PlayerMotion> getPlayersInRange(String playerName);

    PlayerMotion getPlayerMotion(String playerName);

    void leavingPlayer(String playerName);

    Collection<PlayerMotion> getAllMotions();

    void addMotion(CharacterMotionRequest character);
}
