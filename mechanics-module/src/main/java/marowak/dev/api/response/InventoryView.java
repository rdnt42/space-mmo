package marowak.dev.api.response;

import lombok.Builder;
import marowak.dev.api.response.item.ItemView;

import java.util.Collection;

@Builder
public record InventoryView(
        Collection<ItemView> items,
        // TODO REMOVE
        int config
) {
}
