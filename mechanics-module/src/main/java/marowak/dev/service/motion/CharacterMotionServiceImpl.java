package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.world.WorldService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:08
 */

@AllArgsConstructor
@Singleton
public class CharacterMotionServiceImpl implements CharacterMotionService {
    private final WorldService worldService;

    @Override
    public Mono<Void> leavingPlayer(String playerName) {
        worldService.deleteShip(playerName);

        return Mono.empty();
    }

    @Override
    public Flux<BodyInfo> getAllMotions() {
        return worldService.getAllShips();
    }

    @Override
    public void addMotion(CharacterMessage character) {
        CharacterMotion newMotion = new CharacterMotion(
                character.getCharacterName(),
                character.getX(),
                character.getY(),
                character.getAngle(),
                0,
                new Date().getTime());
        worldService.addShip(newMotion);
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
    public Mono<Void> updateShooting(CharacterShootingRequest request, String playerName) {
        worldService.updateShooting(request, playerName);

        return Mono.empty();
    }

    @Override
    public Flux<BodyInfo> getCharactersInRange(String playerName) {
        return worldService.getShipsInRange(playerName);
    }

    @Override
    public Mono<BodyInfo> getCharacter(String playerName) {
        return worldService.getShip(playerName);
    }

}