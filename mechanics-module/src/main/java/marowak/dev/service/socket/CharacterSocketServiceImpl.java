package marowak.dev.service.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.dto.socket.ReceiveSocketMessage;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.bullet.BulletMotionService;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.character.ObjectInfoService;
import marowak.dev.service.command.character.GetCharacterCmd;
import marowak.dev.service.command.character.GetInventoryCmd;
import marowak.dev.service.command.character.GetObjectsInSpaceCmd;
import marowak.dev.service.command.character.UpdateMotionCmd;
import marowak.dev.service.item.CharacterItemService;
import marowak.dev.service.objects.BodyService;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterSocketServiceImpl implements CharacterSocketService {
    private final BulletMotionService bulletMotionService;
    private final ObjectInfoService objectInfoService;
    private final CharacterItemService characterItemService;
    private final CharacterService characterService;
    private final BodyService bodyService;
    private final WebSocketBroadcaster broadcaster;

    private final GetCharacterCmd getCharacterCmd;
    private final GetInventoryCmd getInventoryCmd;
    private final UpdateMotionCmd updateMotionCmd;
    private final GetObjectsInSpaceCmd getObjectsInSpaceCmd;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onOpen(String playerName) {
        Mono.when(
                characterService.sendCharacterState(playerName, true),
                characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ONE, playerName)
        ).subscribe();
    }

    @SneakyThrows
    @Override
    public Publisher<SendSocketMessage<?>> onMessage(String characterName, ReceiveSocketMessage<?> request,
                                                     WebSocketSession session) {
        switch (request.command()) {
            case CMD_GET_CHARACTER -> {
                return getCharacterCmd.execute(characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_GET_INVENTORY -> {
                return getInventoryCmd.execute(characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_MOTION -> {
                return updateMotionCmd.execute(request.data(), characterName)
                        .then(getObjectsInSpaceCmd.execute(characterName))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_INVENTORY_ITEM -> {
                ItemUpdate value = objectMapper.convertValue(request.data(), ItemUpdate.class);
                return characterItemService.updateItem(value, characterName)
                        .map(item -> new SendSocketMessage<>(SendCommandType.CMD_UPDATE_INVENTORY_ITEM, item))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_SHOOTING -> {
                CharacterShootingRequest value = objectMapper.convertValue(request.data(), CharacterShootingRequest.class);
                return bulletMotionService.updateShooting(value, characterName)
                        .thenMany(bodyService.getBullets(characterName))
                        .buffer(200)
                        .map(bullets -> new SendSocketMessage<>(SendCommandType.CMD_UPDATE_SHOOTING, bullets))
                        .flatMap(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            default ->
                    throw new IllegalArgumentException("Unknown or not available message command: " + request.command());
        }
    }

    @Override
    public Publisher<SendSocketMessage<String>> onClose(String playerName) {
        return characterService.sendCharacterState(playerName, false)
                .then(characterService.leavingPlayer(playerName))
                .then(Mono.just(new SendSocketMessage<>(SendCommandType.CMD_LEAVING_PLAYER, playerName)))
                .doOnNext(message -> log.info("Player {} leaving", playerName))
                .flatMapMany(broadcaster::broadcast);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("characterName", String.class, null));
    }
}
