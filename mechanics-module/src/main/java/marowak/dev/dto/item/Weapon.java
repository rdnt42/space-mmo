package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Weapon extends Item {
    private int damage;
    private int radius;
    private int damageTypeId;
    private int equipmentTypeId;
}
