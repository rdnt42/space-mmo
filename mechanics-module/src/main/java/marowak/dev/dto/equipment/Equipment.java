package marowak.dev.dto.equipment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Equipment {
    private long id;
    private int slotId;
    private boolean equipped;
    private int equipmentType;
}
