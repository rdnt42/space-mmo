package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import message.ItemMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class ItemCommandService {
    private final ItemService itemService;
    private final SpaceItemService spaceItemService;

    public Publisher<Void> executeCommand(ItemMessage message) {
        return switch (message.getKey()) {
            case ITEMS_GET_FOR_ALL_CHARACTERS, ITEMS_GET_FOR_ONE_CHARACTER ->
                    itemService.updateInventoryFromStorage(message);
            case ITEMS_GET_IN_SPACE -> spaceItemService.initItem(message);
            default -> Mono.empty();
        };
    }
}
