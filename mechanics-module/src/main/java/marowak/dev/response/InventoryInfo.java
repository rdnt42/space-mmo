package marowak.dev.response;

import lombok.Builder;
import marowak.dev.dto.item.Item;

import java.util.Collection;

@Builder
public record InventoryInfo(
        Collection<Item> items,
        int config
) {
}
