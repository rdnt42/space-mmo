package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class EngineMessage extends EquipmentMessage {
        int speed;

        int upgradeLevel;

        int cost;
}

