package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.dto.motion.PlayerMotionRequest;
import marowak.dev.dto.motion.PlayersMotionListResponse;

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
    private final Map<String, PlayerMotion> playerMotionMap = new ConcurrentHashMap<>();

    @Override
    public void updatePlayerMotion(String playerName, PlayerMotionRequest request) {
        PlayerMotion playerMotion = new PlayerMotion(playerName, request.motion());
        playerMotionMap.put(playerName, playerMotion);
    }

    @Override
    public PlayersMotionListResponse getPlayersMotions(String playerName) {
//        PlayerMotion playerMotion = playerMotionMap.get(playerName);
        List<PlayerMotion> motions = playerMotionMap.values()
                .stream()
                .toList();

        return new PlayersMotionListResponse(motions);
    }
}
