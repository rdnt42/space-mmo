package marowak.dev.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class WeaponView extends ItemView {
    private int damage;
    private int radius;
    private int rate;
    private int damageTypeId;
    private int equipmentTypeId;
}
