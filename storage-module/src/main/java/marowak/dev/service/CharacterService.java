package marowak.dev.service;

import marowak.dev.entity.Character;
import marowak.dev.request.CharacterMotionRequest;
import org.reactivestreams.Publisher;

public interface CharacterService {
    Publisher<Character> create(CharacterMotionRequest request);

    Publisher<Character> updateMotion(CharacterMotionRequest request);

    Publisher<Character> getAllOnline();

    Publisher<Character> get(String characterName);
}
