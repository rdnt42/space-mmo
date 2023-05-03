package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.item.Item;

import java.util.Map;
import java.util.Set;

@Builder
public record CharacterInventory(
        Set<Integer> slots,
        Map<Long, Item> items
) {
}
