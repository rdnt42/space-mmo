package marowak.dev.service.character;

import marowak.dev.request.CharacterMotionRequest;
import reactor.core.publisher.Flux;

public interface CharacterService {
    void sendCharactersUpdate();

    void initCharacters(Flux<CharacterMotionRequest> requests);
}
