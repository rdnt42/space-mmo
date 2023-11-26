package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@Singleton
public class GetWeaponStateCmd implements CharacterCommand<SendSocketMessage<String>> {
    @Override
    public Mono<SendSocketMessage<String>> execute(String characterName) {
        // TODO return some info about weapon
        return Mono.just(new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_WEAPON_INFO, "ok"));
    }
}
