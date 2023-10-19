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
    private int storageId;
    private int typeId;
    private Integer upgradeLevel;
    private Integer cost;
    private String name;
    private String dsc;

    public void init() {
        // base init method
    }

    public void updatePosition(int slotId, int storageId) {
        this.slotId = slotId;
        this.storageId = storageId;
    }
}
