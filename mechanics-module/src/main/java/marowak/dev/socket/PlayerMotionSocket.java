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
@ServerWebSocket("/motion/{characterName}")
public class PlayerMotionSocket {
    private final CharacterMotionSocketService characterMotionSocketService;

    @OnOpen
    public void onOpen(String characterName, WebSocketSession session) {
        debugLog("onOpen", characterName, session);

        characterMotionSocketService.onOpen(characterName);
    }

    @OnMessage
    public Publisher<SocketMessage<?>> onMessage(String characterName, SocketMessage<?> request,
                                                 WebSocketSession session) {
        debugLog("onMessage", characterName, session);

        return characterMotionSocketService.onMessage(characterName, request, session);
    }

    @OnClose
    public Publisher<SocketMessage<String>> onClose(String characterName, WebSocketSession session) {
        debugLog("onClose", characterName, session);

        return characterMotionSocketService.onClose(characterName);
    }

    private void debugLog(String event, String characterName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, characterName: {}", event, session.getId(), characterName);
    }
}
