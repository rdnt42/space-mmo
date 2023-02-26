package marowak.dev.service;

import marowak.dev.entity.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

import java.util.List;

public interface CharacterService {
    Publisher<Character> create(CharacterRequest request);

    Publisher<Character> updateMotion(CharacterRequest request);

    Publisher<Character> updateAllMotions(List<CharacterRequest> requests);
}
