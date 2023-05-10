package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class CargoHookMessage extends ItemMessage {
    private int loadCapacity;

    private int radius;

    private int equipmentTypeId;
}
