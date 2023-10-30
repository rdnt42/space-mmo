package marowak.dev.response;

import lombok.Builder;
import marowak.dev.response.item.ItemView;

import java.util.Collection;

@Builder
public record InventoryView(
        Collection<ItemView> items,
        int config
) {
}
