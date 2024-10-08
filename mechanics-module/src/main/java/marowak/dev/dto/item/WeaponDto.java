package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.api.response.item.WeaponView;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class WeaponDto extends ItemDto {
    private int damage;
    private int radius;
    private int rate;
    private int damageTypeId;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        WeaponView.Builder builder = WeaponView.builder()
                .damage(damage)
                .radius(radius)
                .rate(rate)
                .damageTypeId(damageTypeId)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }

}
