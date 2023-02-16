package marowak.dev.socket;

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
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.service.motion.PlayerMotionSocketService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@ServerWebSocket("/motion/{playerName}")
public class PlayerMotionSocket {
    private final WebSocketBroadcaster broadcaster;
    private final PlayerMotionSocketService playerMotionSocketService;

    @OnOpen
    public Publisher<PlayersMotionListResponse> onOpen(String playerName, WebSocketSession session) {
        debugLog("onOpen", playerName, session);

        PlayersMotionListResponse response = playerMotionSocketService.onOpen(playerName);

        return broadcaster.broadcast(response, filterPlayer(session, playerName));
    }

    @OnMessage
    public Publisher<PlayersMotionListResponse> onMessage(String playerName, PlayerMotionRequest request,
                                                          WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        PlayersMotionListResponse response = playerMotionSocketService.onMessage(request, playerName);

        return broadcaster.broadcast(response, filterPlayer(session, playerName));
    }

    @OnClose
    public Publisher<PlayerLeavingResponse> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);
        PlayerLeavingResponse response = playerMotionSocketService.onClose(playerName);

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
