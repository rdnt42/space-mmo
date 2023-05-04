package marowak.dev.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.SocketMessage;
import marowak.dev.dto.item.Item;
import marowak.dev.enums.MessageCommand;
import marowak.dev.request.CharacterInventoryItemRequest;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.response.player.CharacterInventoryResponse;
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.motion.PlayerMotionService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class PlayerMotionSocketServiceImpl implements PlayerMotionSocketService {
    private final PlayerMotionService playerMotionService;

    private final ItemService itemService;
    private final CharacterService characterService;
    private final WebSocketBroadcaster broadcaster;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onOpen(String playerName) {
        characterService.sendCharacterState(playerName, true);
        characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ONE, playerName);
    }

    @SneakyThrows
    @Override
    public Publisher<SocketMessage<?>> onMessage(String playerName, SocketMessage<?> request,
                                                                          WebSocketSession session) {
        SocketMessage<?> socketResponse;
        switch (request.command()) {
            case CMD_GET_MOTIONS -> {
                PlayersMotionListResponse response = playerMotionService.getMotions(playerName);
                socketResponse = new SocketMessage<>(MessageCommand.CMD_GET_MOTIONS, response);
            }
            case CMD_GET_INVENTORY -> {
                CharacterInventoryResponse response = itemService.getInventory(playerName);
                socketResponse = new SocketMessage<>(MessageCommand.CMD_GET_INVENTORY, response);
            }
            case CMD_UPDATE_MOTION -> {
                CharacterMotionRequest value = objectMapper.convertValue(request.data(), CharacterMotionRequest.class);
                PlayersMotionListResponse response =
                        playerMotionService.updateAndGetMotions(value, playerName);
                socketResponse = new SocketMessage<>(MessageCommand.CMD_UPDATE_MOTION, response);
            }
            case CMD_UPDATE_INVENTORY_ITEM -> {
                CharacterInventoryItemRequest value = objectMapper.convertValue(request.data(), CharacterInventoryItemRequest.class);
                Item item = itemService.updateInventoryFromClient(value, playerName);

                socketResponse = new SocketMessage<>(MessageCommand.CMD_UPDATE_INVENTORY_ITEM, item);
            }
            default ->
                    throw new IllegalArgumentException("Unknown or not available message command: " + request.command());

        }

        return broadcaster.broadcast(socketResponse, filterOtherPlayers(session, playerName));
    }

    @Override
    public Publisher<SocketMessage<String>> onClose(String playerName) {
        characterService.sendCharacterState(playerName, false);
        playerMotionService.leavingPlayer(playerName);

        SocketMessage<String> socketResponse = new SocketMessage<>(MessageCommand.CMD_LEAVING_PLAYER, playerName);

        return broadcaster.broadcast(socketResponse);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("playerName", String.class, null));
    }
}
