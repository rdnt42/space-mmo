package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.service.WorldService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<CharacterMotion> getAllMotions() {
        return worldService.getAllShips();
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