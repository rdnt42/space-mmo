package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

import static marowak.dev.enums.SendCommandType.CMD_BLOW_UP_CHARACTER;

@Singleton
public class GetExplodingCharacter implements CharacterCommand<SendSocketMessage<String>> {
    @Override
    public Mono<SendSocketMessage<String>> execute(String characterName) {
        return Mono.just(new SendSocketMessage<>(CMD_BLOW_UP_CHARACTER, characterName));
    }
}
