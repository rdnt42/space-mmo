package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.ObjectsInSpace;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.ObjectInfoService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class GetObjectsInSpaceCmd implements CharacterCommand<SendSocketMessage<ObjectsInSpace>> {
    private final ObjectInfoService objectInfoService;

    @Override
    public Mono<SendSocketMessage<ObjectsInSpace>> execute(String characterName) {
        return objectInfoService.getCharactersInRange(characterName)
                .collectList()
                .zipWith(objectInfoService.getItemsInRange(characterName)
                        .collectList(), ObjectsInSpace::new)
                .map(data -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_SPACE_OBJECTS, data));
    }
}
