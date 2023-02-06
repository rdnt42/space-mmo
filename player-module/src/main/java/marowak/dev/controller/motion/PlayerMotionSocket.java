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
import marowak.dev.dto.motion.PlayerMotionRequest;
import marowak.dev.dto.motion.PlayerMotionListResponse;
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
    public Publisher<PlayerMotionListResponse> onOpen(String playerName, WebSocketSession session) {
        debugLog("onOpen", playerName, session);

        playerMotionService.initPlayerMotion(playerName);
        PlayerMotionListResponse response = playerMotionService.getPlayersMotions(playerName);

        return broadcaster.broadcast(response, isValid(session, playerName));
    }

    @OnMessage
    public Publisher<PlayerMotionListResponse> onMessage(String playerName, PlayerMotionRequest request,
                                                         WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        if (request.isUpdate()) {
            playerMotionService.updatePlayerMotion(playerName, request);
        }

        PlayerMotionListResponse response = playerMotionService.getPlayersMotions(playerName);

        return broadcaster.broadcast(response, isValid(session, playerName));
    }

    @OnClose
    public Publisher<String> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);
        playerMotionService.deletePlayer(playerName);

        return broadcaster.broadcast(String.format("[%s] Leaving!", playerName), isValid(session, playerName));
    }

    private Predicate<WebSocketSession> isValid(WebSocketSession session, String playerName) {
        // TODO need validation
        return s -> true;
    }

    private void debugLog(String event, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, playerName: {}", event, session.getId(), playerName);
    }
}
