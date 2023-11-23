package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.api.response.item.ItemView;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Item {
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

    public void init() {
        // base init method
    }

    protected ItemView.ItemViewBuilder<?, ?> getItemBuilder(ItemView.ItemViewBuilder<?, ?> builder) {
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

    public ItemView getView() {
        return getItemBuilder(ItemView.builder())
                .build();
    }

    public void updateStorage(int slotId, int storageId) {
        this.slotId = slotId;
        this.storageId = storageId;
    }
}
