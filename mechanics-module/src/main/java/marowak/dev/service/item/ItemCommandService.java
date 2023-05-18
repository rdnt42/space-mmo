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

    public Publisher<Void> executeCommand(ItemMessage message) {
        return switch (message.getKey()) {
            case ITEMS_GET_ALL, ITEMS_GET_ONE -> itemService.updateInventoryFromStorage(message);
            default -> Mono.empty();
        };
    }
}
