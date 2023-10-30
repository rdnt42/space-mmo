package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.response.item.HullView;
import marowak.dev.response.item.ItemView;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Hull extends Item {
    private int hp;
    private int evasion;
    private int armor;
    private int equipmentTypeId;
    private int config;

    private SpaceShipBody body;

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
