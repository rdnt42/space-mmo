package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketBroadcaster;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.SocketMessage;
import reactor.core.publisher.Mono;

import static marowak.dev.enums.MessageCommand.CMD_BLOW_UP_CHARACTER;

@RequiredArgsConstructor
@Singleton
public class CharacterInformerSocketServiceImpl implements CharacterInformerSocketService {

    private final WebSocketBroadcaster broadcaster;

    @Override
    public <T> Mono<SocketMessage<T>> sendExplosionToAll(T data) {
        var message = new SocketMessage<T>(CMD_BLOW_UP_CHARACTER, data);

        return Mono.from(broadcaster.broadcast(message));
    }
}
