package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.HullView;
import marowak.dev.api.response.item.ItemView;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class HullDto extends ItemDto {
    private int hp;
    private int evasion;
    private int armor;
    private int equipmentTypeId;
    private int config;

    @Override
    public ItemView getView() {
        HullView.HullViewBuilder<?, ?> builder = HullView.builder()
                .hp(hp)
                .evasion(evasion)
                .armor(armor)
                .equipmentTypeId(equipmentTypeId)
                .config(config);

        return super.getItemBuilder(builder)
                .build();
    }
}
