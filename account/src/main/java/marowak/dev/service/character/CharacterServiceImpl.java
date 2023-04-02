package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:55
 */
@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterClient characterClient;

    @Override
    public void createCharacter(CharacterRequest request, String username) {
        characterClient.sendCharacter(CharacterMessageKey.CHARACTER_CREATE, request)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for create character {}, account {}", r.accountName(), r.characterName()))
                .subscribe();
    }
}
