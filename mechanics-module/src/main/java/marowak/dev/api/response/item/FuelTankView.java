package marowak.dev.api.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class FuelTankView extends ItemView {
    private int capacity;
    private int equipmentTypeId;
}
