package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.service.WorldService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

@AllArgsConstructor
@Singleton
public class CharacterMotionServiceImpl implements CharacterMotionService {
    private final Map<String, CharacterMotion> playerMotionMap = new ConcurrentHashMap<>();
    private final WorldService worldService;
    private final int DOUBLED_PLAYERS_IN_RANGE = 1000 * 1000;

    @Override
    public void leavingPlayer(String playerName) {
        playerMotionMap.remove(playerName);
    }

    @Override
    public Collection<CharacterMotion> getAllMotions() {
        return playerMotionMap.values();
    }

    @Override
    public void addMotion(CharacterMessage character) {
        CharacterMotion newMotion = new CharacterMotion(character.getCharacterName(), character.getX(), character.getY(),
                character.getAngle(), 0, new Date().getTime());
        worldService.addShip(newMotion);
//        playerMotionMap.put(character.getCharacterName(), newMotion);
    }

    @Override
    public Mono<Void> updateMotion(CharacterMotionRequest request, String playerName) {
        Mono<Void> result = Mono.empty();
        if (!request.isUpdate()) {
            return result;
        }

        worldService.updateShip(request, playerName);
        return Mono.empty();


//        CharacterMotion oldMotion = playerMotionMap.get(playerName);
//
//        long diffTime = request.lastUpdateTime() - oldMotion.lastUpdateTime();
//        if (diffTime < 0) return result;
//
//        float relativeSpeed = (request.speed() * diffTime) / 1000;
//
//        double newX = oldMotion.x() + getXShift(relativeSpeed, request.angle());
//        BigDecimal fx = BigDecimal.valueOf(newX).setScale(2, RoundingMode.HALF_UP);
//
//        double newY = oldMotion.y() + getYShift(relativeSpeed, request.angle());
//        BigDecimal fy = BigDecimal.valueOf(newY).setScale(2, RoundingMode.HALF_UP);
//
//        CharacterMotion newMotion = new CharacterMotion(playerName, fx.doubleValue(), fy.doubleValue(), request.angle(),
//                request.speed(), request.lastUpdateTime());
//
//        playerMotionMap.put(playerName, newMotion);
    }

    private double getXShift(float speed, int angle) {
        return Math.cos(Math.toRadians(angle)) * speed;
    }

    private double getYShift(float speed, int angle) {
        return Math.sin(Math.toRadians(angle)) * speed;
    }

    @Override
    public Flux<CharacterMotion> getCharactersInRange(String playerName) {
//        CharacterMotion player = playerMotionMap.get(playerName);
        return worldService.getShips(playerName);
    }

    @Override
    public Mono<CharacterMotion> getCharacter(String playerName) {
        return worldService.getShip(playerName);
    }

    private boolean isInRange(CharacterMotion base, CharacterMotion target) {
        double diffX = base.x() - target.x();
        double diffY = base.y() - target.y();

        return (diffX * diffX + diffY * diffY) <= DOUBLED_PLAYERS_IN_RANGE;
    }

}