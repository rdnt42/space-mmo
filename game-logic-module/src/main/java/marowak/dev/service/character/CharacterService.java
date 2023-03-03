package marowak.dev.service.character;

import marowak.dev.request.CharacterRequest;
import reactor.core.publisher.Flux;

public interface CharacterService {
    void sendCharactersUpdate();

    void initCharacters(Flux<CharacterRequest> requests);
}
