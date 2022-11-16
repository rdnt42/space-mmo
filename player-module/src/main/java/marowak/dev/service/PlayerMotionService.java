package marowak.dev.service;

import marowak.dev.dto.PlayerMotion;
import marowak.dev.dto.PlayerMotionList;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:21
 */
public interface PlayerMotionService {
    void updatePlayerMotion(PlayerMotion request);

    PlayerMotionList getPlayersMotions();
}
