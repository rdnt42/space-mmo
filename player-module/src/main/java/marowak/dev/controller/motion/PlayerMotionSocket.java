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
import marowak.dev.dto.motion.PlayersMotionListResponse;
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

        return broadcaster.broadcast(response, isValid(session, playerName));
    }

    @OnMessage
    public Publisher<PlayersMotionListResponse> onMessage(String playerName, PlayerMotionRequest request,
                                                          WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        if (request.isUpdate()) {
            playerMotionService.updatePlayerMotion(playerName, request);
        }

        PlayersMotionListResponse response = playerMotionService.getPlayersMotions(playerName);

        return broadcaster.broadcast(response, isValid(session, playerName));
    }

    @OnClose
    public Publisher<String> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);

        return broadcaster.broadcast(String.format("[%s] Leaving!", playerName), isValid(session, playerName));
    }

    private Predicate<WebSocketSession> isValid(WebSocketSession session, String playerName) {
//        return s -> s != session &&
//                topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null)) &&
//                playerName.equalsIgnoreCase(s.getUriVariables().get("playerName", String.class, null));
        return s -> true;
    }

    private void debugLog(String event, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, playerName: {}", event, session.getId(), playerName);
    }
}
