package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class HullMessage extends ItemMessage {
    private int hp;

    private int evasion;

    private int armor;

    private int equipmentTypeId;
}
