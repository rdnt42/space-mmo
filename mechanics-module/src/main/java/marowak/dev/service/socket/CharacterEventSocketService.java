package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.socket.ReceiveSocketMessage;
import marowak.dev.dto.socket.SendSocketMessage;
import org.reactivestreams.Publisher;

@Slf4j
@RequiredArgsConstructor
@ServerWebSocket("/motion/{characterName}")
public class CharacterEventSocketService {
    private final CharacterSocketService characterSocketService;

    @OnOpen
    public void onOpen(String characterName, WebSocketSession session) {
        debugLog("onOpen", characterName, session);

        characterSocketService.onOpen(characterName);
    }

    @OnMessage
    public Publisher<SendSocketMessage<?>> onMessage(String characterName, ReceiveSocketMessage<?> request,
                                                     WebSocketSession session) {
        debugLog("onMessage", characterName, session);

        return characterSocketService.onMessage(characterName, request, session);
    }

    @OnClose
    public Publisher<SendSocketMessage<String>> onClose(String characterName, WebSocketSession session) {
        debugLog("onClose", characterName, session);

        return characterSocketService.onClose(characterName);
    }

    private void debugLog(String event, String characterName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, characterName: {}", event, session.getId(), characterName);
    }
}
