package marowak.dev.dto.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equipment {
    private final int id;
    private final int slotId;
    private final boolean equipped;
    private final int equipmentType;
}
