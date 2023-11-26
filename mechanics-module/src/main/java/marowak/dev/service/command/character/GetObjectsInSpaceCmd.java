package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.BulletBodyView;
import marowak.dev.api.response.CharacterView;
import marowak.dev.api.response.ObjectsInSpace;
import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.character.ObjectInfoService;
import marowak.dev.service.command.CharacterCommand;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Singleton
public class GetObjectsInSpaceCmd implements CharacterCommand<SendSocketMessage<ObjectsInSpace>> {
    private final ObjectInfoService objectInfoService;

    @Override
    public Mono<SendSocketMessage<ObjectsInSpace>> execute(String characterName) {
        Mono<List<CharacterView>> characters = objectInfoService.getCharactersInRange(characterName)
                .collectList();
        Mono<List<ItemInSpaceView>> items = objectInfoService.getItemsInRange(characterName)
                .collectList();
        Mono<List<BulletBodyView>> bullets = objectInfoService.getBullets(characterName)
                .collectList();
        return Mono.zip(characters, items, bullets)
                .map(t -> new ObjectsInSpace(t.getT1(), t.getT2(), t.getT3()))
                .map(data -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_SPACE_OBJECTS, data));
    }
}
