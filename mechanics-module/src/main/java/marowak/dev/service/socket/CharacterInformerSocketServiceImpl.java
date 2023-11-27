package marowak.dev.service.socket;

import io.micronaut.websocket.WebSocketBroadcaster;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.service.command.character.GetExplodingCharacter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class CharacterInformerSocketServiceImpl implements CharacterInformerSocketService {

    private final WebSocketBroadcaster broadcaster;
    private final GetExplodingCharacter getExplodingCharacter;

    @Override
    public Mono<SendSocketMessage<String>> sendExplosionToAll(String characterName) {
        return getExplodingCharacter.execute(characterName)
                .flatMap(message -> Mono.from(broadcaster.broadcast(message)));
    }
}
