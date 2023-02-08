package marowak.dev.controller.motion;

import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayerLeavingResponse;
import marowak.dev.response.player.PlayerMotionResponse;
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.service.PlayerMotionService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
@ServerWebSocket("/motion/{playerName}")
public class PlayerMotionSocket {
    private final WebSocketBroadcaster broadcaster;
    private final PlayerMotionService playerMotionService;

    @OnOpen
    public Publisher<PlayersMotionListResponse> onOpen(String playerName, WebSocketSession session) {
        debugLog("onOpen", playerName, session);

        playerMotionService.initPlayerMotion(playerName);
        PlayersMotionListResponse response = playerMotionService.getPlayersMotions(playerName);

        return broadcaster.broadcast(response, filterPlayer(session, playerName));
    }

    @OnMessage
    public Publisher<PlayerMotionResponse> onMessage(String playerName, PlayerMotionRequest request,
                                                          WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        if (request.isUpdate()) {
            playerMotionService.updatePlayerMotion(playerName, request);
        }

        PlayerMotionResponse response = playerMotionService.getPlayerMotion(playerName);

        return broadcaster.broadcast(response);
    }

    @OnClose
    public Publisher<PlayerLeavingResponse> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);
        PlayerLeavingResponse response = playerMotionService.leavingPlayer(playerName);

        return broadcaster.broadcast(response);
    }

    private Predicate<WebSocketSession> filterPlayer(WebSocketSession session, String playerName) {
        return s -> s.getId().equals(session.getId()) &&
                playerName.equalsIgnoreCase(s.getUriVariables()
                        .get("playerName", String.class, null));
    }

    private void debugLog(String event, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, playerName: {}", event, session.getId(), playerName);
    }
}
