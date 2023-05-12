package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.request.CharacterMotionRequest;
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
    private final int DOUBLED_PLAYERS_IN_RANGE = 1000 * 1000;

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
        PlayerMotion newMotion = new PlayerMotion(character.getCharacterName(), character.getX(), character.getY(),
                character.getAngle(), 0, new Date().getTime());

        playerMotionMap.put(character.getCharacterName(), newMotion);
    }

    @Override
    public PlayersMotionListResponse updateAndGetMotions(CharacterMotionRequest request, String playerName) {
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
    private void updatePlayerMotion(String playerName, CharacterMotionRequest request) {
        PlayerMotion oldMotion = playerMotionMap.get(playerName);

        long diffTime = request.lastUpdateTime() - oldMotion.lastUpdateTime();
        if (diffTime < 0) return;

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
        PlayerMotion player = playerMotionMap.get(playerName);

        return playerMotionMap.values().stream()
                .filter(v -> !v.playerName().equals(playerName))
                .filter(v -> isInRange(player, v))
                .toList();
    }

    private boolean isInRange(PlayerMotion base, PlayerMotion target) {
        double diffX = base.x() - target.x();
        double diffY = base.y() - target.y();

        return (diffX * diffX + diffY * diffY) <= DOUBLED_PLAYERS_IN_RANGE;
    }

}