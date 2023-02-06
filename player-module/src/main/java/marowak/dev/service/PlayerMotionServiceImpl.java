package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.Motion;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.dto.motion.PlayerMotionRequest;
import marowak.dev.dto.motion.PlayerMotionListResponse;

import java.util.List;
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
    // FIXME just for training
    private final int MAP_HEIGHT = 10000;
    private final int MAP_WIDTH = 10000;

    private final Map<String, PlayerMotion> playerMotionMap = new ConcurrentHashMap<>();

    @Override
    public void updatePlayerMotion(String playerName, PlayerMotionRequest request) {
        PlayerMotion playerMotion = new PlayerMotion(playerName, request.motion());
        playerMotionMap.put(playerName, playerMotion);
    }

    @Override
    public PlayerMotionListResponse getPlayersMotions(String playerName) {
        List<PlayerMotion> motions = playerMotionMap.values()
                .stream()
                .toList();

        PlayerMotion playerMotion = playerMotionMap.get(playerName);

        return new PlayerMotionListResponse(playerMotion.motion(), motions);
    }

    @Override
    public void initPlayerMotion(String playerName) {
        playerMotionMap.put(playerName, getInitMotion(playerName));
    }

    @Override
    public void deletePlayer(String playerName) {
        playerMotionMap.remove(playerName);
    }

    private PlayerMotion getInitMotion(String playerName) {
        Motion motion = new Motion(MAP_WIDTH / 2, MAP_HEIGHT / 2);

        return new PlayerMotion(playerName, motion);
    }
}
