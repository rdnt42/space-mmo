package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.response.player.PlayerMotionListResponse;
import marowak.dev.response.player.PlayerMotionResponse;

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
    private final Map<String, PlayerMotionResponse> playerMotionMap = new ConcurrentHashMap<>();

    @Override
    public void updatePlayerMotion(PlayerMotionResponse request) {
        playerMotionMap.put(request.playerName(), request);
    }

    @Override
    public PlayerMotionListResponse getPlayersMotions() {
        List<PlayerMotionResponse> motions = playerMotionMap.values()
                .stream()
                .toList();

        return new PlayerMotionListResponse(motions);
    }
}
