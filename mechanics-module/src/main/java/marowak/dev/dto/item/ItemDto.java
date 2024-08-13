package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.ItemView;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ItemDto {
    private long id;
    private String characterName;
    private int slotId;
    private int storageId;
    private int typeId;
    private Integer upgradeLevel;
    private Integer cost;
    private String name;
    private String dsc;
    private Double x;
    private Double y;

    public ItemView.Builder getItemBuilder(ItemView.Builder builder) {
        return builder
                .id(id)
                .slotId(slotId)
                .storageId(storageId)
                .typeId(typeId)
                .upgradeLevel(upgradeLevel)
                .cost(cost)
                .name(name)
                .dsc(dsc);
    }

    public abstract ItemView getView();
}
