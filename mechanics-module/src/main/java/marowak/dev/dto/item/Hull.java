package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Hull extends Item {
    private int hp;
    private int evasion;
    private int armor;
    private int equipmentTypeId;
    private int config;
}
