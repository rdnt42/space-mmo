package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.Motion;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;

import java.util.Collection;
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
    public Collection<PlayerMotion> getPlayersInRange(String playerName) {
        // TODO added range filter
        return playerMotionMap.values();
    }

    @Override
    public PlayerMotion getPlayerMotion(String playerName) {
        return playerMotionMap.get(playerName);
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
        Motion newMotion = new Motion(character.x(), character.y(), character.angle(), 0);

        PlayerMotion playerMotion = new PlayerMotion(character.characterName(), newMotion);
        playerMotionMap.put(character.characterName(), playerMotion);
    }

    @Override
    public PlayersMotionListResponse updateAndGetMotions(PlayerMotionRequest request, String playerName) {
        if (request.isUpdate()) {
            updatePlayerMotion(playerName, request);
        }

        Collection<PlayerMotion> motions = getPlayersInRange(playerName);
        PlayerMotion currPlayerMotion = getPlayerMotion(playerName);

        return new PlayersMotionListResponse(currPlayerMotion, motions);
    }
}
