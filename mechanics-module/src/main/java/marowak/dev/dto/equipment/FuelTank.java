package marowak.dev.dto.equipment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FuelTank extends Equipment{
    @Builder
    public FuelTank(int id, int slotId,  boolean equipped, int equipmentType) {
        super(id, slotId, equipped, equipmentType);
    }
}
