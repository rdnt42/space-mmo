package marowak.dev.service.socket;

import marowak.dev.dto.socket.SendSocketMessage;
import reactor.core.publisher.Mono;

public interface CharacterInformerSocketService {
    Mono<SendSocketMessage<String>> sendExplosionToAll(String characterName);
}
