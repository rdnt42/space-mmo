package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.response.item.EngineView;
import marowak.dev.response.item.ItemView;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Engine extends Item {
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

