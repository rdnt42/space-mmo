package marowak.dev.service;

import io.micronaut.websocket.WebSocketSession;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.response.player.SocketResponse;
import org.reactivestreams.Publisher;

public interface PlayerMotionSocketService {
    void onOpen(String playerName);

    Publisher<SocketResponse<PlayersMotionListResponse>> onMessage(String playerName, PlayerMotionRequest request, WebSocketSession session);

    Publisher<SocketResponse<String>> onClose(String playerName);
}
