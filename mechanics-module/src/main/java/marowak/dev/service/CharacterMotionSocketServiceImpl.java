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
import marowak.dev.enums.MessageCommand;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.motion.CharacterMotionService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterMotionSocketServiceImpl implements CharacterMotionSocketService {
    private final CharacterMotionService characterMotionService;

    private final CharacterInfoService characterInfoService;

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
    public Publisher<SocketMessage<?>> onMessage(String characterName, SocketMessage<?> request,
                                                 WebSocketSession session) {
        switch (request.command()) {
            case CMD_GET_MOTIONS -> {
                return characterInfoService.getCharactersInfo(characterName)
                        .map(info -> new SocketMessage<>(MessageCommand.CMD_GET_MOTIONS, info))
                        .doOnNext(resp -> log.info(""))
                        .flatMap(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_GET_INVENTORY -> {
                return itemService.getItems(characterName)
                        .map(item -> new SocketMessage<>(MessageCommand.CMD_GET_INVENTORY, item))
                        .flatMap(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            default -> {
                return null;
            }
        }

//            case CMD_UPDATE_MOTION -> {
//                CharacterMotionRequest value = objectMapper.convertValue(request.data(), CharacterMotionRequest.class);
//                if (value.isUpdate()) {
//                    characterMotionService.updateMotion(value, characterName);
//                }
////                socketResponse = new SocketMessage<>(MessageCommand.CMD_UPDATE_MOTION, response);
//            }
//            case CMD_UPDATE_INVENTORY_ITEM -> {
//                ItemUpdate value = objectMapper.convertValue(request.data(), ItemUpdate.class);
//                ItemUpdate item = itemService.updateInventoryFromClient(value, characterName);
//
////                socketResponse = new SocketMessage<>(MessageCommand.CMD_UPDATE_INVENTORY_ITEM, item);
//            }
//            default ->
//                    throw new IllegalArgumentException("Unknown or not available message command: " + request.command());
//
//        }
//
//        return broadcaster.broadcast(socketResponse, filterOtherPlayers(session, characterName));
    }

    @Override
    public Publisher<SocketMessage<String>> onClose(String playerName) {
        characterService.sendCharacterState(playerName, false);
        characterMotionService.leavingPlayer(playerName);

        SocketMessage<String> socketResponse = new SocketMessage<>(MessageCommand.CMD_LEAVING_PLAYER, playerName);

        return broadcaster.broadcast(socketResponse);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("characterName", String.class, null));
    }
}
