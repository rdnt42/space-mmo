package marowak.dev.service.command.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.InventoryView;
import marowak.dev.dto.socket.SendSocketMessage;
import marowak.dev.enums.SendCommandType;
import marowak.dev.service.command.CharacterCommand;
import marowak.dev.service.item.CharacterItemService;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class GetInventoryCmd implements CharacterCommand<SendSocketMessage<InventoryView>> {
    private final CharacterItemService characterItemService;

    @Override
    public Mono<SendSocketMessage<InventoryView>> execute(String characterName) {
        return characterItemService.getInventory(characterName)
                .map(info -> new SendSocketMessage<>(SendCommandType.CMD_RECEIVE_INVENTORY, info))
                .doOnSuccess(c -> log.info("Get inventory for character: {}", characterName));
    }
}
