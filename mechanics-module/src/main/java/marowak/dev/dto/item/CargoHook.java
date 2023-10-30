package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.response.item.CargoHookView;
import marowak.dev.response.item.ItemView;

@Getter
@NoArgsConstructor
@SuperBuilder
public class CargoHook extends Item {
    private int loadCapacity;
    private int radius;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        CargoHookView.CargoHookViewBuilder<?, ?> builder = CargoHookView.builder()
                .loadCapacity(loadCapacity)
                .radius(radius)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }
}
