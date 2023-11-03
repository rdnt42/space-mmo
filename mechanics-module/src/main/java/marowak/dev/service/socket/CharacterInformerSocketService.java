package marowak.dev.service.socket;

import marowak.dev.dto.SocketMessage;
import reactor.core.publisher.Mono;

public interface CharacterInformerSocketService {
    <T> Mono<SocketMessage<T>> sendExplosionToAll(T data);
}
