package marowak.dev.service.item;

import message.ItemMessage;
import reactor.core.publisher.Flux;

public interface ItemService {
    Flux<ItemMessage> getAllOnline();

    Flux<ItemMessage> getForCharacter(String characterName);
}
