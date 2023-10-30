package marowak.dev.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class HullView extends ItemView {
    private int hp;
    private int evasion;
    private int armor;
    private int equipmentTypeId;
    private int config;
}
