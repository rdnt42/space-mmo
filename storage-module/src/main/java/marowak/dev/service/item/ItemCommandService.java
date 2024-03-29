package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import message.ItemMessage;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class ItemCommandService {
    private final ItemService itemService;

    public Publisher<ItemMessage> executeCommand(ItemMessage message) {
        return switch (message.getKey()) {
            case ITEMS_GET_FOR_ONE_CHARACTER -> itemService.getForCharacter(message.getCharacterName());
            case ITEMS_GET_FOR_ALL_CHARACTERS -> itemService.getOnline();
            case ITEMS_GET_IN_SPACE -> itemService.getItemsInSpace();
            case ITEM_UPDATE, ITEM_UPDATE_IN_SPACE -> itemService.updateItem(message);
            case ITEM_DELETE -> itemService.deleteItem(message);
        };
    }

}
