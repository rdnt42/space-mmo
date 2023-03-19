package marowak.dev.socket;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.SocketResponse;
import marowak.dev.service.PlayerMotionSocketService;
import org.reactivestreams.Publisher;

@Slf4j
@RequiredArgsConstructor
@ServerWebSocket("/motion/{playerName}")
public class PlayerMotionSocket {
    private final PlayerMotionSocketService playerMotionSocketService;

    @OnOpen
    public void onOpen(String playerName, WebSocketSession session) {
        debugLog("onOpen", playerName, session);

        playerMotionSocketService.onOpen(playerName);
    }

    @OnMessage
    public Publisher<SocketResponse<?>> onMessage(String playerName, PlayerMotionRequest request,
                                                          WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        return playerMotionSocketService.onMessage(playerName, request, session);
    }

    @OnClose
    public Publisher<SocketResponse<?>> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);

        return playerMotionSocketService.onClose(playerName);
    }

    private void debugLog(String event, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, playerName: {}", event, session.getId(), playerName);
    }
}
