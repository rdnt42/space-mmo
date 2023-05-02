package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Item {
    private long id;
    private int slotId;
    private boolean equipped;
    private int itemTypeId;
    private int upgradeLevel;
    private int cost;
}
