package marowak.dev.service;

import marowak.dev.entity.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

public interface CharacterService {
    Publisher<Character> create(CharacterRequest request);

    Publisher<Character> updateMotion(CharacterRequest request);
}
