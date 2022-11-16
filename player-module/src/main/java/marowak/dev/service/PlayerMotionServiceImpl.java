package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.dto.PlayerMotion;
import marowak.dev.dto.PlayerMotionList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:08
 */
@Singleton
public class PlayerMotionServiceImpl implements PlayerMotionService {
    private final Map<String, PlayerMotion> playerMotionMap = new ConcurrentHashMap<>();

    @Override
    public void updatePlayerMotion(PlayerMotion request) {
        playerMotionMap.put(request.getPlayerName(), request);
    }

    @Override
    public PlayerMotionList getPlayersMotions() {
        return PlayerMotionList.builder()
                .playerMotions(playerMotionMap.values()
                        .stream().toList())
                .build();
    }
}
