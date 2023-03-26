package marowak.dev.service;

import io.micronaut.websocket.WebSocketSession;
import marowak.dev.dto.SocketMessage;
import marowak.dev.response.player.PlayersMotionListResponse;
import org.reactivestreams.Publisher;

public interface PlayerMotionSocketService {
    void onOpen(String playerName);

    Publisher<SocketMessage<PlayersMotionListResponse>> onMessage(String playerName, SocketMessage<?> request, WebSocketSession session);

    Publisher<SocketMessage<String>> onClose(String playerName);
}
