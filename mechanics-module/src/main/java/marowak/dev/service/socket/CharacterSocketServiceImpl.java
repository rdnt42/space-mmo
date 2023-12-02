package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.socket.ReceiveSocketMessage;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.service.command.character.*;
import marowak.dev.service.command.item.TakeItemFromSpaceCmd;
import marowak.dev.service.command.item.UpdateCharacterItemCmd;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterSocketServiceImpl implements CharacterSocketService {
    private final WebSocketBroadcaster broadcaster;

    private final GetCharacterCmd getCharacterCmd;
    private final GetInventoryCmd getInventoryCmd;
    private final UpdateMotionCmd updateMotionCmd;
    private final GetObjectsInSpaceCmd getObjectsInSpaceCmd;
    private final UpdateCharacterItemCmd updateCharacterItemCmd;
    private final UpdateShootingCmd updateShootingCmd;
    private final GetWeaponStateCmd getWeaponStateCmd;
    private final InitCharacterCmd initCharacterCmd;
    private final LeavingPlayerCmd leavingPlayerCmd;
    private final TakeItemFromSpaceCmd takeItemFromSpaceCmd;

    @Override
    public void onOpen(String characterName) {
        initCharacterCmd.execute(characterName).subscribe();
    }

    @SneakyThrows
    @Override
    public Publisher<SendSocketMessage<?>> onMessage(String characterName, ReceiveSocketMessage<?> request,
                                                     WebSocketSession session) {
        // TODO add common mapper for requests
        // create message as template
        switch (request.command()) {
            case CMD_UPDATE_MOTION -> {
                return updateMotionCmd.execute(request.data(), characterName)
                        .then(getObjectsInSpaceCmd.execute(characterName))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_SHOOTING -> {
                return updateShootingCmd.execute(request.data(), characterName)
                        .then(getWeaponStateCmd.execute(characterName))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_INVENTORY_ITEM -> {
                return updateCharacterItemCmd.execute(request.data(), characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_GET_CHARACTER -> {
                return getCharacterCmd.execute(characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_GET_INVENTORY -> {
                return getInventoryCmd.execute(characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_TAKE_ITEM_FROM_SPACE -> {
                return takeItemFromSpaceCmd.execute(request.data(), characterName)
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            default ->
                    throw new IllegalArgumentException("Unknown or not available message command: " + request.command());
        }
    }

    @Override
    public Publisher<SendSocketMessage<String>> onClose(String characterName) {
        return leavingPlayerCmd.execute(characterName)
                .flatMapMany(broadcaster::broadcast);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("characterName", String.class, null));
    }
}
