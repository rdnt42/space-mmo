package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.item.Item;

import java.util.Map;

@Builder
public record CharacterInventory(
        Map<Long, Item> items
) {
}
