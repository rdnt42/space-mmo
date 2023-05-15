package marowak.dev.socket;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.SocketMessage;
import marowak.dev.service.CharacterMotionSocketService;
import org.reactivestreams.Publisher;

@Slf4j
@RequiredArgsConstructor
@ServerWebSocket("/motion/{playerName}")
public class PlayerMotionSocket {
    private final CharacterMotionSocketService characterMotionSocketService;

    @OnOpen
    public void onOpen(String playerName, WebSocketSession session) {
        debugLog("onOpen", playerName, session);

        characterMotionSocketService.onOpen(playerName);
    }

    @OnMessage
    public Publisher<SocketMessage<?>> onMessage(String playerName, SocketMessage<?> request,
                                                                          WebSocketSession session) {
        debugLog("onMessage", playerName, session);

        return characterMotionSocketService.onMessage(playerName, request, session);
    }

    @OnClose
    public Publisher<SocketMessage<String>> onClose(String playerName, WebSocketSession session) {
        debugLog("onClose", playerName, session);

        return characterMotionSocketService.onClose(playerName);
    }

    private void debugLog(String event, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, characterName: {}", event, session.getId(), playerName);
    }
}
