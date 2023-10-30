package marowak.dev.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class CargoHookView extends ItemView {
    private int loadCapacity;
    private int radius;
    private int equipmentTypeId;
}
