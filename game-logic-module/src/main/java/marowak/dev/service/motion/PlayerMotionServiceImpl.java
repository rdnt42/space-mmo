package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;

import java.util.Collection;
import java.util.Collections;
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
        PlayerMotion oldMotion = playerMotionMap.get(playerName);

        int newX = oldMotion.x() + getXShift(request.speed(), request.angle());
        int newY = oldMotion.y() + getYShift(request.speed(), request.angle());
        PlayerMotion newMotion = new PlayerMotion(playerName, newX, newY, request.angle(), request.speed());

        playerMotionMap.put(playerName, newMotion);
    }

    private int getXShift(int speed, int angle) {
        return (int) (Math.cos(Math.toRadians(angle)) * speed);
    }

    private int getYShift(int speed, int angle) {
        return (int) (Math.sin(Math.toRadians(angle)) * speed);
    }

    @Override
    public Collection<PlayerMotion> getPlayersInRange(String playerName) {
        // TODO added range filter
        return playerMotionMap.values();
    }

    @Override
    public PlayerMotion getPlayerMotion(String playerName) {
        return playerMotionMap.getOrDefault(playerName, null);
    }

    @Override
    public void leavingPlayer(String playerName) {
        playerMotionMap.remove(playerName);
    }

    @Override
    public Collection<PlayerMotion> getAllMotions() {
        return playerMotionMap.values();
    }

    @Override
    public void addMotion(CharacterMotionRequest character) {
        PlayerMotion newMotion = new PlayerMotion(character.characterName(), character.x(), character.y(), character.angle(), 0);

        playerMotionMap.put(character.characterName(), newMotion);
    }

    @Override
    public PlayersMotionListResponse updateAndGetMotions(PlayerMotionRequest request, String playerName) {
        PlayerMotion currPlayerMotion = getPlayerMotion(playerName);
        if (currPlayerMotion == null) {
            return new PlayersMotionListResponse(null, Collections.emptyList());
        }

        if (request.isUpdate()) {
            updatePlayerMotion(playerName, request);
        }

        Collection<PlayerMotion> motions = getPlayersInRange(playerName);

        return new PlayersMotionListResponse(currPlayerMotion, motions);
    }
}