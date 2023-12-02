package marowak.dev.service.command.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.TakeItemFromSpaceRequest;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import marowak.dev.service.item.SpaceItemService;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class TakeItemFromSpaceCmd implements CharacterRequestCommand<Object, SendSocketMessage<ItemView>> {
    private final CharacterItemService characterItemService;
    private final SpaceItemService spaceItemService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<SendSocketMessage<ItemView>> execute(Object request, String characterName) {
        TakeItemFromSpaceRequest value = objectMapper.convertValue(request, TakeItemFromSpaceRequest.class);

        return spaceItemService.removeItem(value.id())
                .flatMap(removedId -> characterItemService.addItemFromSpace(removedId, characterName))
                .doOnSuccess(i -> log.info("Character {} got item {} from space", characterName, i.getId()))
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_ITEM, info));
    }
}
