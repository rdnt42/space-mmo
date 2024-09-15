package message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import keys.ItemMessageKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonSerialize
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EngineMessage.class, name = "engine"),
        @JsonSubTypes.Type(value = FuelTankMessage.class, name = "fuel_tank"),
        @JsonSubTypes.Type(value = CargoHookMessage.class, name = "cargo_hook"),
        @JsonSubTypes.Type(value = HullMessage.class, name = "hull"),
        @JsonSubTypes.Type(value = WeaponMessage.class, name = "weapon"),
})
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class ItemMessage {
    private final ItemMessageKey key;
    private final Long id;
    private final Integer slotId;
    private final Integer storageId;
    private final String characterName;
    private final int typeId;
    private final Integer upgradeLevel;
    private final Integer cost;
    private final String name;
    private final String dsc;
    private final Double x;
    private final Double y;

    public ItemMessage(Builder builder) {
        this.key = builder.key;
        this.id = builder.id;
        this.slotId = builder.slotId;
        this.storageId = builder.storageId;
        this.characterName = builder.characterName;
        this.typeId = builder.typeId;
        this.upgradeLevel = builder.upgradeLevel;
        this.cost = builder.cost;
        this.name = builder.name;
        this.dsc = builder.dsc;
        this.x = builder.x;
        this.y = builder.y;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copy() {
        return new Builder()
                .key(key)
                .id(id)
                .slotId(slotId)
                .storageId(storageId)
                .characterName(characterName)
                .typeId(typeId)
                .upgradeLevel(upgradeLevel)
                .cost(cost)
                .name(name)
                .dsc(dsc)
                .x(x)
                .y(y);
    }

    protected void setDataFromParent(Builder builder) {
        builder
                .key(key)
                .id(id)
                .slotId(slotId)
                .storageId(storageId)
                .characterName(characterName)
                .typeId(typeId)
                .upgradeLevel(upgradeLevel)
                .cost(cost)
                .name(name)
                .dsc(dsc)
                .x(x)
                .y(y);
    }

    public static class Builder {
        private ItemMessageKey key;
        private Long id;
        private Integer slotId;
        private Integer storageId;
        private String characterName;
        private int typeId;
        private Integer upgradeLevel;
        private Integer cost;
        private String name;
        private String dsc;
        private Double x;
        private Double y;

        public ItemMessage build() {
            return new ItemMessage(this);
        }

        public Builder key(ItemMessageKey key) {
            this.key = key;
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder slotId(Integer slotId) {
            this.slotId = slotId;
            return this;
        }

        public Builder storageId(Integer storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder characterName(String characterName) {
            this.characterName = characterName;
            return this;
        }

        public Builder typeId(int typeId) {
            this.typeId = typeId;
            return this;
        }

        public Builder upgradeLevel(Integer upgradeLevel) {
            this.upgradeLevel = upgradeLevel;
            return this;
        }

        public Builder cost(Integer cost) {
            this.cost = cost;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder dsc(String dsc) {
            this.dsc = dsc;
            return this;
        }

        public Builder x(Double x) {
            this.x = x;
            return this;
        }

        public Builder y(Double y) {
            this.y = y;
            return this;
        }
    }
}
