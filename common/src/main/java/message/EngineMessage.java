package message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EngineMessage implements EquipmentMark {
        Long id;

        int slotId;

        boolean equipped;

        String characterName;

        int engineTypeId;

        int speed;

        int upgradeLevel;

        int cost;
}

