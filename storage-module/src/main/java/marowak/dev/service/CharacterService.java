package marowak.dev.service;

import marowak.dev.entity.Character;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterStateRequest;
import org.reactivestreams.Publisher;

public interface CharacterService {
    Publisher<Character> create(CharacterMotionRequest request);

    Publisher<Character> updateMotion(CharacterMotionRequest request);

    Publisher<Character> getAllOnline();

    Publisher<Character> get(String characterName);

    Publisher<Character> updateState(CharacterStateRequest request);
}
