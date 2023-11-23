package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketSession;
import marowak.dev.dto.socket.ReceiveSocketMessage;
import marowak.dev.dto.socket.SendSocketMessage;
import org.reactivestreams.Publisher;

public interface CharacterSocketService {
    void onOpen(String playerName);

    Publisher<SendSocketMessage<?>> onMessage(String playerName, ReceiveSocketMessage<?> request, WebSocketSession session);

    Publisher<SendSocketMessage<String>> onClose(String playerName);
}
