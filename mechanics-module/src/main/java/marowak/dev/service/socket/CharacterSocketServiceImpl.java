package marowak.dev.service.socket;

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
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.request.ItemUpdate;
import marowak.dev.service.bullet.BulletMotionService;
import marowak.dev.service.character.CharacterInfoService;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.objects.BodyService;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterSocketServiceImpl implements CharacterSocketService {
    private final BulletMotionService bulletMotionService;
    private final CharacterInfoService characterInfoService;
    private final ItemService itemService;
    private final CharacterService characterService;
    private final BodyService bodyService;
    private final WebSocketBroadcaster broadcaster;

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
    public Publisher<SocketMessage<?>> onMessage(String characterName, SocketMessage<?> request,
                                                 WebSocketSession session) {
        switch (request.command()) {
            case CMD_GET_MOTIONS -> {
                return characterInfoService.getCharacterInfo(characterName)
                        .doOnNext(c -> log.info("New character {} get characters info", characterName))
                        .map(info -> new SocketMessage<>(MessageCommand.CMD_GET_MOTIONS, info))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_GET_INVENTORY -> {
                return itemService.getInventoryItems(characterName)
                        .doOnNext(c -> log.info("New character {} get inventory info", characterName))
                        .map(item -> new SocketMessage<>(MessageCommand.CMD_GET_INVENTORY, item))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_MOTION -> {
                CharacterMotionRequest value = objectMapper.convertValue(request.data(), CharacterMotionRequest.class);
                return characterService.updateCharacterPosition(value, characterName)
                        .thenMany(characterInfoService.getCharactersInRangeInfo(characterName))
                        .buffer(50)
                        .map(infoList -> new SocketMessage<>(MessageCommand.CMD_UPDATE_MOTION, infoList))
                        .flatMap(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_INVENTORY_ITEM -> {
                ItemUpdate value = objectMapper.convertValue(request.data(), ItemUpdate.class);
                return itemService.updateInventoryFromClient(value, characterName)
                        .map(item -> new SocketMessage<>(MessageCommand.CMD_UPDATE_INVENTORY_ITEM, item))
                        .flatMapMany(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            case CMD_UPDATE_SHOOTING -> {
                CharacterShootingRequest value = objectMapper.convertValue(request.data(), CharacterShootingRequest.class);
                return bulletMotionService.updateShooting(value, characterName)
                        .thenMany(bodyService.getBullets(characterName))
                        .buffer(200)
                        .map(bullets -> new SocketMessage<>(MessageCommand.CMD_UPDATE_SHOOTING, bullets))
                        .flatMap(resp -> broadcaster.broadcast(resp, filterOtherPlayers(session, characterName)));
            }
            default ->
                    throw new IllegalArgumentException("Unknown or not available message command: " + request.command());
        }
    }

    @Override
    public Publisher<SocketMessage<String>> onClose(String playerName) {
        return characterService.sendCharacterState(playerName, false)
                .then(characterService.leavingPlayer(playerName))
                .then(Mono.just(new SocketMessage<>(MessageCommand.CMD_LEAVING_PLAYER, playerName)))
                .doOnNext(message -> log.info("Player {} leaving", playerName))
                .flatMapMany(broadcaster::broadcast);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("characterName", String.class, null));
    }
}
