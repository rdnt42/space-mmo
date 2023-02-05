package marowak.dev.service;

import marowak.dev.dto.motion.PlayerMotionRequest;
import marowak.dev.dto.motion.PlayersMotionListResponse;
import marowak.dev.response.player.PlayerMotionResponse;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void updatePlayerMotion(String playerName, PlayerMotionRequest request);

    PlayersMotionListResponse getPlayersMotions(String playerName);

    void initPlayerMotion(String playerName);
}
