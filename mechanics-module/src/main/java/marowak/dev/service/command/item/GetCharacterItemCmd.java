package marowak.dev.service.command.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.ObjectInfoService;
import marowak.dev.service.command.CharacterRequestCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class GetCharacterItemCmd implements CharacterRequestCommand<Long, SendSocketMessage<ItemView>> {
    private final ObjectInfoService objectInfoService;

    @Override
    public Mono<SendSocketMessage<ItemView>> execute(Long request, String characterName) {
        return objectInfoService.getItem(request)
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_UPDATE_INVENTORY_ITEM, info));
    }
}
