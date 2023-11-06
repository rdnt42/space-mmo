package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.FuelTankView;
import marowak.dev.api.response.item.ItemView;

@Getter
@NoArgsConstructor
@SuperBuilder
public class FuelTank extends Item {
    private int capacity;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        FuelTankView.FuelTankViewBuilder<?, ?> builder = FuelTankView.builder()
                .capacity(capacity)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }
}
