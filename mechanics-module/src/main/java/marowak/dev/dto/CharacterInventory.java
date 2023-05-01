package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.equipment.Cargo;
import marowak.dev.dto.equipment.Equipment;

import java.util.Map;
import java.util.Set;

@Builder
public record CharacterInventory(
        Set<Integer> slots,
        Map<Long, Equipment> equipments,
        Map<Long, Cargo> cargos
) {
}
