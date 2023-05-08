package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class FuelTankMessage extends ItemMessage {
    private int capacity;

    private int equipmentTypeId;
}

