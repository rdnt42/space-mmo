package marowak.dev.service.command.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.ItemUpdateRequest;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.ObjectInfoService;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class UpdateCharacterItemCmd implements CharacterRequestCommand<Object, SendSocketMessage<ItemView>> {
    private final CharacterItemService characterItemService;
    private final ObjectInfoService objectInfoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<SendSocketMessage<ItemView>> execute(Object request, String characterName) {
        ItemUpdateRequest value = objectMapper.convertValue(request, ItemUpdateRequest.class);

        return characterItemService.updateItem(value, characterName)
                .flatMap(updated -> objectInfoService.getItem(updated.id()))
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_UPDATE_INVENTORY_ITEM, info));
    }
}
