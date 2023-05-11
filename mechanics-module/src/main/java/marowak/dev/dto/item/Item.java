package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Item {
    private long id;
    private Integer slotId;
    private int typeId;
    private Integer upgradeLevel;
    private Integer cost;
    private String name;
    private String dsc;
}