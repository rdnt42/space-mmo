package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class LeavingPlayerCmd implements CharacterCommand<SendSocketMessage<String>> {
    private final CharacterService characterService;

    @Override
    public Mono<SendSocketMessage<String>> execute(String characterName) {
        return characterService.sendCharacterState(characterName, false)
                .then(characterService.leavingPlayer(characterName))
                .then(Mono.just(new SendSocketMessage<>(SendCommandType.CMD_LEAVING_PLAYER, characterName)))
                .doOnNext(message -> log.info("Player {} leaving", characterName));
    }
}
