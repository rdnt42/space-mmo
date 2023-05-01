package marowak.dev.dto.equipment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Engine extends Equipment {
    private int speed;
    private int upgradeLevel;
    private int cost;
}
