package marowak.dev.service;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.CharactersGetMessageKey;
import marowak.dev.enums.MessageCommand;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.response.player.SocketResponse;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.motion.PlayerMotionService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class PlayerMotionSocketServiceImpl implements PlayerMotionSocketService {
    private final PlayerMotionService playerMotionService;
    private final CharacterService characterService;
    private final WebSocketBroadcaster broadcaster;

    @Override
    public void onOpen(String playerName) {
        characterService.sendCharacterState(playerName, true);
        characterService.sendInitCharacter(CharactersGetMessageKey.CHARACTERS_GET_ONE, playerName);
    }

    @Override
    public Publisher<SocketResponse<?>> onMessage(String playerName, PlayerMotionRequest request,
                                                                          WebSocketSession session) {
        PlayersMotionListResponse response = playerMotionService.updateAndGetMotions(request, playerName);

        SocketResponse<PlayersMotionListResponse> socketResponse = new SocketResponse<>(MessageCommand.CMD_UPDATE_CURRENT_PLAYER, response);

        return broadcaster.broadcast(socketResponse, filterOtherPlayers(session, playerName));
    }

    @Override
    public Publisher<SocketResponse<?>> onClose(String playerName) {
        characterService.sendCharacterState(playerName, false);
        playerMotionService.leavingPlayer(playerName);

        SocketResponse<String> socketResponse = new SocketResponse<>(MessageCommand.CMD_LEAVING_PLAYER, playerName);

        return broadcaster.broadcast(socketResponse);
    }

    private Predicate<WebSocketSession> filterOtherPlayers(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("playerName", String.class, null));
    }

}
