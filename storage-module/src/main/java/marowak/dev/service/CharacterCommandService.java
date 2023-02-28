package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

import java.util.List;

@RequiredArgsConstructor
@Singleton
public class CharacterCommandService {
    private final CharacterService characterService;

    public <T> Publisher<Character> executeCommand(CharacterMessageKey key, T request) {
        return switch (key) {
            case CHARACTER_CREATE -> characterService.create((CharacterRequest) request);
            case CHARACTER_UPDATE -> characterService.updateMotion((CharacterRequest) request);
            case CHARACTER_UPDATE_ALL -> characterService.updateAllMotions((List<CharacterRequest>) request);
        };
    }
}
