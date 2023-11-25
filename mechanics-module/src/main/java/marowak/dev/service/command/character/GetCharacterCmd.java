package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.CharacterView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.ObjectInfoService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class GetCharacterCmd implements CharacterCommand<SendSocketMessage<CharacterView>> {
    private final ObjectInfoService objectInfoService;

    @Override
    public Mono<SendSocketMessage<CharacterView>> execute(String characterName) {
        return objectInfoService.getCharacter(characterName)
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_CHARACTER, info))
                .doOnSuccess(c -> log.info("Get character info for character: {}", characterName));
    }
}
