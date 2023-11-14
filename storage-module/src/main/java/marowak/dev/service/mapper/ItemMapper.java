package marowak.dev.service.mapper;

import marowak.dev.entity.Item;
import message.ItemMessage;

public interface ItemMapper<T> {
    ItemMessage map(Item item, T extension);
}
