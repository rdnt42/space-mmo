package marowak.dev.service.command.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.SpaceItemService;
import message.ItemMessage;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class AddSpaceItemCmd implements CharacterRequestCommand<ItemMessage, Void> {
    private final SpaceItemService spaceItemService;

    @Override
    public Mono<Void> execute(ItemMessage request, String characterName) {
        return spaceItemService.addItem(request);
    }
}
