package marowak.dev.dto.equipment;

import lombok.Builder;

public class FuelTank extends Equipment{
    private final int fuelTankTypeId;

    @Builder
    public FuelTank(int id, int slotId, int fuelTankTypeId) {
        super(id, slotId);
        this.fuelTankTypeId = fuelTankTypeId;
    }
}
