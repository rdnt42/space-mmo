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
    private static final int MAP_HEIGHT = 10000;
    private static final int MAP_WIDTH = 10000;

    private final Map<String, PlayerMotion> playerMotionMap = new ConcurrentHashMap<>();

    @Override
    public void updatePlayerMotion(String playerName, PlayerMotionRequest request) {
        Motion oldMotion = playerMotionMap.get(playerName)
                .motion();
        int newX = oldMotion.x() + getXShift(request.speed(), request.angle());
        int newY = oldMotion.y() + getYShift(request.speed(), request.angle());
        Motion newMotion = new Motion(newX, newY, request.angle(), request.speed());

        PlayerMotion playerMotion = new PlayerMotion(playerName, newMotion);
        playerMotionMap.put(playerName, playerMotion);
    }

    private int getXShift(int speed, int angle) {
        return (int)(Math.cos(Math.toRadians(angle)) * speed);
    }

    private int getYShift(int speed, int angle) {
        return (int)(Math.sin(Math.toRadians(angle)) * speed);
    }

    @Override
    public PlayersMotionListResponse getPlayersMotions(String playerName) {
        List<PlayerMotion> motions = playerMotionMap.values()
                .stream()
                .toList();

        PlayerMotion playerMotion = playerMotionMap.get(playerName);

        return new PlayersMotionListResponse(MessageCommand.CMD_UPDATE_CURRENT_PLAYER, playerMotion, motions);
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
        Motion motion = new Motion(MAP_WIDTH / 2, MAP_HEIGHT / 2, 270, 0);

        return new PlayerMotion(playerName, motion);
    }
}
