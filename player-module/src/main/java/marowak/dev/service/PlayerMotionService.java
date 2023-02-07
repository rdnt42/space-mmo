package marowak.dev.service;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayerMotionResponse;
import marowak.dev.response.player.PlayersMotionListResponse;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void updatePlayerMotion(String playerName, PlayerMotionRequest request);

    PlayersMotionListResponse getPlayersMotions(String playerName);

    PlayerMotionResponse getPlayerMotion(String playerName);

    void initPlayerMotion(String playerName);

    void deletePlayer(String playerName);
}
