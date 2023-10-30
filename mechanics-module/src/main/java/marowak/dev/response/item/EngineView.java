package marowak.dev.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class EngineView extends ItemView {
    private int speed;
    private int jump;
    private int equipmentTypeId;
}
