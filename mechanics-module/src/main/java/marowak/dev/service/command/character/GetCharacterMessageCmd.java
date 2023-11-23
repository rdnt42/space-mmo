package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.CharacterView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class GetCharacterMessageCmd implements CharacterCommand<SendSocketMessage<CharacterView>> {
    private final CharacterShipService characterShipService;

    @Override
    public Mono<SendSocketMessage<CharacterView>> execute(String characterName) {
        return characterShipService.getCharacter(characterName)
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_CHARACTER, info))
                .doOnSuccess(c -> log.info("New character {} get characters info", characterName));
    }
}
