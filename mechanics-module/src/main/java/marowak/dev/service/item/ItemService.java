package marowak.dev.service.item;

import keys.ItemMessageKey;
import message.ItemMessage;

public interface ItemService {
    void sendGetItems(ItemMessageKey key, String characterName);

    void updateItem(ItemMessage message);
}
