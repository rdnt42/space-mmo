package marowak.dev.dto;

import lombok.Builder;
import marowak.dev.dto.equipment.Cargo;
import marowak.dev.dto.equipment.Equipment;

import java.util.Collection;

@Builder
public record PlayerInventory(
        Collection<Integer> slots,
        Collection<Equipment> equipments,
        Collection<Cargo> cargos
) {
}
