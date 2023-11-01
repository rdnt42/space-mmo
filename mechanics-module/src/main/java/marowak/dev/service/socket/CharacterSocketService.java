package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketSession;
import marowak.dev.dto.SocketMessage;
import org.reactivestreams.Publisher;

public interface CharacterSocketService {
    void onOpen(String playerName);

    Publisher<SocketMessage<?>> onMessage(String playerName, SocketMessage<?> request, WebSocketSession session);

    Publisher<SocketMessage<String>> onClose(String playerName);
}
