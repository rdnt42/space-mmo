package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.Motion;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.MessageCommand;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayerLeavingResponse;
import marowak.dev.response.player.PlayerMotionResponse;
import marowak.dev.response.player.PlayersMotionListResponse;

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
    public PlayersMotionListResponse getPlayersMotions(String playerName) {
        List<PlayerMotion> motions = playerMotionMap.values()
                .stream()
                .toList();

        PlayerMotion playerMotion = playerMotionMap.get(playerName);

        return new PlayersMotionListResponse(MessageCommand.CMD_UPDATE_ALL, playerMotion, motions);
    }

    @Override
    public PlayerMotionResponse getPlayerMotion(String playerName) {
        return new PlayerMotionResponse(MessageCommand.CMD_UPDATE_OTHER_PLAYER, playerMotionMap.get(playerName));
    }

    @Override
    public void initPlayerMotion(String playerName) {
        playerMotionMap.put(playerName, getInitMotion(playerName));
    }

    @Override
    public PlayerLeavingResponse leavingPlayer(String playerName) {
        playerMotionMap.remove(playerName);

        return new PlayerLeavingResponse(MessageCommand.CMD_LEAVING_PLAYER, playerName);
    }

    private PlayerMotion getInitMotion(String playerName) {
        Motion motion = new Motion(MAP_WIDTH / 2, MAP_HEIGHT / 2);

        return new PlayerMotion(playerName, motion);
    }
}
