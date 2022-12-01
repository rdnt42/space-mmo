package marowak.dev.service;

import marowak.dev.response.player.PlayerMotionListResponse;
import marowak.dev.response.player.PlayerMotionResponse;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void updatePlayerMotion(PlayerMotionResponse request);

    PlayerMotionListResponse getPlayersMotions();
}
