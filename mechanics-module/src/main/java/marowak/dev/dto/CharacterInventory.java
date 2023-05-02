package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.item.Cargo;
import marowak.dev.dto.item.Item;

import java.util.Map;
import java.util.Set;

@Builder
public record CharacterInventory(
        Set<Integer> slots,
        Map<Long, Item> items,
        Map<Long, Cargo> cargos
) {
}
