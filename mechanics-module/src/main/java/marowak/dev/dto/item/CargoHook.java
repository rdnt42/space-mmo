package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class CargoHook extends Item {
    private int loadCapacity;
    private int radius;
    private int equipmentTypeId;
}
