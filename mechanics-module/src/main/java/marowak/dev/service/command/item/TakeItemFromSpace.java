package marowak.dev.service.command.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import marowak.dev.service.item.SpaceItemService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class TakeItemFromSpace implements CharacterRequestCommand<Long, ItemView> {
    private final CharacterItemService characterItemService;
    private final SpaceItemService spaceItemService;

    @Override
    public Mono<ItemView> execute(Long request, String characterName) {
        return spaceItemService.removeItem(request)
                .flatMap(id -> characterItemService.addItemFromSpace(id, characterName));
    }
}
