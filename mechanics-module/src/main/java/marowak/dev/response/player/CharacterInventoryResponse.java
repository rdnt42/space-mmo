package marowak.dev.response.player;

import lombok.Builder;
import marowak.dev.dto.item.Cargo;
import marowak.dev.dto.item.Item;

import java.util.Collection;

@Builder
public record CharacterInventoryResponse(
        Collection<Integer> slots,
        Collection<? extends Item> items,
        Collection<Cargo> cargos
) {
}
