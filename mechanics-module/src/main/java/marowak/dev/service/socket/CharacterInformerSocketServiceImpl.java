package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketBroadcaster;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.socket.SendSocketMessage;
import reactor.core.publisher.Mono;

import static marowak.dev.enums.SendCommandType.CMD_BLOW_UP_CHARACTER;

@RequiredArgsConstructor
@Singleton
public class CharacterInformerSocketServiceImpl implements CharacterInformerSocketService {

    private final WebSocketBroadcaster broadcaster;

    @Override
    public <T> Mono<SendSocketMessage<T>> sendExplosionToAll(T data) {
        var message = new SendSocketMessage<>(CMD_BLOW_UP_CHARACTER, data);

        return Mono.from(broadcaster.broadcast(message));
    }
}
