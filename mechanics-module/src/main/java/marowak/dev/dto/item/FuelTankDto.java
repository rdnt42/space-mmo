package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.FuelTankView;
import marowak.dev.api.response.item.ItemView;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class FuelTankDto extends ItemDto {
    private int capacity;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        FuelTankView.Builder builder = FuelTankView.builder()
                .capacity(capacity)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }
}
