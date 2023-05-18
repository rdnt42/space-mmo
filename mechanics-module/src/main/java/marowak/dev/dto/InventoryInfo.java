package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.item.Item;

import java.util.Collection;

@Builder
public record InventoryInfo(
        Collection<Item> items,
        int config
) {
}
