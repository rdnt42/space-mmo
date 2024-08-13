package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.CargoHookView;
import marowak.dev.api.response.item.ItemView;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class CargoHookDto extends ItemDto {
    private int loadCapacity;
    private int radius;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        CargoHookView.Builder builder = CargoHookView.builder()
                .loadCapacity(loadCapacity)
                .radius(radius)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }
}
