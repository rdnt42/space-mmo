package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.EngineView;
import marowak.dev.api.response.item.ItemView;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class EngineDto extends ItemDto {
    private int speed;
    private int jump;
    private int equipmentTypeId;

    @Override
    public ItemView getView() {
        EngineView.EngineViewBuilder<?, ?> builder = EngineView.builder()
                .speed(speed)
                .jump(jump)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }
}

