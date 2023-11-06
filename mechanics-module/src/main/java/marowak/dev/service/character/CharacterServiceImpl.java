package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.api.response.CharacterView;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.item.ItemService;
import message.CharacterMessage;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharactersClient charactersClient;
    private final CharacterShipService characterShipService;
    private final CharacterInfoService characterInfoService;
    private final ItemService itemService;

    @Override
    public Mono<Void> sendCharactersUpdate() {
        return characterInfoService.getAllMotions()
                .map(infoToMessage)
                .doOnError(e -> log.error("Send characters updating failed", e))
                .flatMap(charactersClient::sendCharacters)
                .then();
    }

    @Override
    public Mono<Void> initCharacter(CharacterMessage message) {
        CharacterMotion request = new CharacterMotion(
                message.getCharacterName(),
                message.getX(),
                message.getY(),
                message.getAngle(),
                0);

        return characterShipService.addCharacter(request)
                .doOnNext(character -> log.info("Character init successful, key: {}, character name: {}", message.getKey(), character.getId()))
                .doOnError(e -> log.error("Character init failed", e))
                .then(itemService.sendGetItems(ItemMessageKey.ITEMS_GET_ONE, message.getCharacterName()))
                .then();
    }

    @Override
    public Mono<Void> leavingPlayer(String characterName) {
        return characterShipService.removeCharacter(characterName)
                .then();
    }

    @Override
    public Mono<Void> sendCharacterState(String characterName, boolean isOnline) {
        CharacterMessage message = CharacterMessage.builder()
                .key(CharacterMessageKey.CHARACTER_STATE_UPDATE)
                .characterName(characterName)
                .online(isOnline)
                .build();

        return charactersClient.sendCharacters(message)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .then();
    }

    @Override
    public Mono<Void> sendInitCharacter(CharacterMessageKey key, String characterName) {
        CharacterMessage message = CharacterMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();

        return charactersClient.sendCharacters(message)
                .doOnError(e -> log.error("Send Characters init error, key: {}, name: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send Character init, key: {}, name: {}", key, characterName))
                .then();
    }

    @Override
    public Mono<Void> updateCharacterPosition(CharacterMotionRequest request, String characterName) {
        if (!request.isUpdate()) {
            return Mono.empty();
        }

        return characterShipService.updateShipPosition(request, characterName);
    }

    private final Function<CharacterView, CharacterMessage> infoToMessage = info -> CharacterMessage.builder()
            .key(CharacterMessageKey.CHARACTER_MOTION_UPDATE)
            .characterName(info.characterName())
            .x(info.x())
            .y(info.y())
            .angle(info.angle())
            .build();

}
