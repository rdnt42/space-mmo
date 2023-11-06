package marowak.dev.api.response.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ItemView {
    private long id;
    private int slotId;
    private int storageId;
    private int typeId;
    private Integer upgradeLevel;
    private Integer cost;
    private String name;
    private String dsc;
}
