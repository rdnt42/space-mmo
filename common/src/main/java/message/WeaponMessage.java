package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class WeaponMessage extends ItemMessage {
    private int damage;

    private int radius;

    private int damageTypeId;

    private int equipmentTypeId;
}
