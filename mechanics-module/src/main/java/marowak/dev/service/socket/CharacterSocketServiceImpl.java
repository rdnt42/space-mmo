package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.socket.ReceiveSocketMessage;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.command.character.*;
import marowak.dev.service.command.item.GetCharacterItemCmd;
import marowak.dev.service.command.item.UpdateCharacterItemCmd;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterSocketServiceImpl implements CharacterSocketService {
    private final CharacterService characterService;
    private final WebSocketBroadcaster broadcaster;

    private final GetCharacterCmd getCharacterCmd;
    private final GetInventoryCmd getInventoryCmd;
    private final UpdateMotionCmd updateMotionCmd;
    private final GetObjectsInSpaceCmd getObjectsInSpaceCmd;
    private final UpdateCharacterItemCmd updateCharacterItemCmd;
    private final GetCharacterItemCmd getCharacterItemCmd;
    private final UpdateShootingCmd updateShootingCmd;
    private final GetWeaponStateCmd getWeaponStateCmd;


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
                return updateCharacterItemCmd.execute(request.data(), characterName)
                        .flatMap(itemUpdate -> getCharacterItemCmd.execute(itemUpdate.id(), characterName))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_SHOOTING -> {
                return updateShootingCmd.execute(request.data(), characterName)
                        .then(getWeaponStateCmd.execute(characterName))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
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
