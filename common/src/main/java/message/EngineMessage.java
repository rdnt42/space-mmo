package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
public class EngineMessage extends EquipmentMessage {
        String characterName;

        int engineTypeId;

        int speed;

        int upgradeLevel;

        int cost;
}

