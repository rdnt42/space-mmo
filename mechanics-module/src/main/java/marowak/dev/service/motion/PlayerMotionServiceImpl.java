package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;
import message.CharacterMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
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
    public void leavingPlayer(String playerName) {
        playerMotionMap.remove(playerName);
    }

    @Override
    public Collection<PlayerMotion> getAllMotions() {
        return playerMotionMap.values();
    }

    @Override
    public void addMotion(CharacterMessage character) {
        PlayerMotion newMotion = new PlayerMotion(character.getAccountName(), character.getX(), character.getY(),
                character.getAngle(), 0, new Date().getTime());

        playerMotionMap.put(character.getCharacterName(), newMotion);
    }

    @Override
    public PlayersMotionListResponse updateAndGetMotions(PlayerMotionRequest request, String playerName) {
        if (request.isUpdate()) {
            updatePlayerMotion(playerName, request);
        }

        PlayerMotion currPlayerMotion = playerMotionMap.get(playerName);
        Collection<PlayerMotion> motions = getPlayersInRange(playerName);

        return new PlayersMotionListResponse(currPlayerMotion, motions);
    }

    @Override
    public PlayersMotionListResponse getMotions(String playerName) {
        PlayerMotion currPlayerMotion = playerMotionMap.get(playerName);
        if (currPlayerMotion == null) {
            return null;
        }
        Collection<PlayerMotion> motions = getPlayersInRange(playerName);

        return new PlayersMotionListResponse(currPlayerMotion, motions);
    }

    @Override
    public boolean isPlayerInit(String playerName) {
        return playerMotionMap.containsKey(playerName);
    }

    private void updatePlayerMotion(String playerName, PlayerMotionRequest request) {
        PlayerMotion oldMotion = playerMotionMap.get(playerName);

        long diffTime = request.lastUpdateTime() - oldMotion.lastUpdateTime();
        if(diffTime < 0) return;

        float relativeSpeed = (request.speed() * diffTime) / 1000;

        double newX = oldMotion.x() + getXShift(relativeSpeed, request.angle());
        BigDecimal fx = BigDecimal.valueOf(newX).setScale(2, RoundingMode.HALF_UP);

        double newY = oldMotion.y() + getYShift(relativeSpeed, request.angle());
        BigDecimal fy = BigDecimal.valueOf(newY).setScale(2, RoundingMode.HALF_UP);

        PlayerMotion newMotion = new PlayerMotion(playerName, fx.doubleValue(), fy.doubleValue(), request.angle(),
                request.speed(), request.lastUpdateTime());

        playerMotionMap.put(playerName, newMotion);
    }

    private double getXShift(float speed, int angle) {
        return Math.cos(Math.toRadians(angle)) * speed;
    }

    private double getYShift(float speed, int angle) {
        return Math.sin(Math.toRadians(angle)) * speed;
    }

    private Collection<PlayerMotion> getPlayersInRange(String playerName) {
        // TODO added range filter
        return playerMotionMap.values().stream()
                .filter(v -> !v.playerName().equals(playerName))
                .toList();
    }

}