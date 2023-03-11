package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class CharacterCommandService {
    private final CharacterService characterService;

    public Publisher<Character> executeCommand(CharacterMessageKey key, CharacterRequest request) {
        return switch (key) {
            case CHARACTER_CREATE -> characterService.create((CharacterMotionRequest) request);
            case CHARACTER_UPDATE -> characterService.updateMotion((CharacterMotionRequest)request);
        };
    }
}
