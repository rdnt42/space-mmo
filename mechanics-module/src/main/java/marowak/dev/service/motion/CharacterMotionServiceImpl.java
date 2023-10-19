package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.physic.ShipService;
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

    private final ShipService shipService;
    private final CharacterShipService characterShipService;

    @Override
    public Mono<Void> leavingPlayer(String playerName) {
        return shipService.deleteShip(playerName);
    }

    @Override
    public Flux<BodyInfo> getAllMotions() {
        return shipService.getAllShips();
    }

    @Override
    public Mono<Void> addMotion(CharacterMessage character) {
        CharacterMotion newMotion = new CharacterMotion(
                character.getCharacterName(),
                character.getX(),
                character.getY(),
                character.getAngle(),
                0,
                new Date().getTime());

        return characterShipService.addShip(newMotion);
    }

    @Override
    public Mono<Void> updateMotion(CharacterMotionRequest request, String playerName) {
        Mono<Void> result = Mono.empty();
        if (!request.isUpdate()) {
            return result;
        }

        return shipService.updateShip(request, playerName);
    }

    @Override
    public Flux<BodyInfo> getCharactersInRange(String playerName) {
        return shipService.getShipsInRange(playerName);
    }

    @Override
    public Mono<BodyInfo> getCharacter(String playerName) {
        return shipService.getShip(playerName);
    }

}