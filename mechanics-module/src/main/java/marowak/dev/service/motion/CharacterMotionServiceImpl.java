package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.service.character.CharacterShipService;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:08
 */

@AllArgsConstructor
@Singleton
public class CharacterMotionServiceImpl implements CharacterMotionService {
    private final CharacterShipService characterShipService;

    @Override
    public Mono<Void> leavingPlayer(String characterName) {
        return characterShipService.removeCharacter(characterName);
    }


    @Override
    public Mono<Void> addMotion(CharacterMotion request) {
        return characterShipService.addCharacter(request);
    }

    @Override
    public Mono<Void> updateMotion(CharacterMotionRequest request, String characterName) {
        if (!request.isUpdate()) {
            return Mono.empty();
        }

        return characterShipService.updateShipPosition(request, characterName);
    }
}