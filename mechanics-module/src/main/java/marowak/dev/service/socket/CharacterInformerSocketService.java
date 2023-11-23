package marowak.dev.service.socket;

import marowak.dev.dto.socket.SendSocketMessage;
import reactor.core.publisher.Mono;

public interface CharacterInformerSocketService {
    <T> Mono<SendSocketMessage<T>> sendExplosionToAll(T data);
}
