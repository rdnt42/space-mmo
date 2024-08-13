package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class ItemView {
    private final long id;
    private final int slotId;
    private final int storageId;
    private final int typeId;
    private final Integer upgradeLevel;
    private final Integer cost;
    private final String name;
    private final String dsc;

    public ItemView(Builder builder) {
        this.id = builder.id;
        this.slotId = builder.slotId;
        this.storageId = builder.storageId;
        this.typeId = builder.typeId;
        this.upgradeLevel = builder.upgradeLevel;
        this.cost = builder.cost;
        this.name = builder.name;
        this.dsc = builder.dsc;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private int slotId;
        private int storageId;
        private int typeId;
        private Integer upgradeLevel;
        private Integer cost;
        private String name;
        private String dsc;

        public ItemView build() {
            return new ItemView(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder slotId(int slotId) {
            this.slotId = slotId;
            return this;
        }

        public Builder storageId(int storageId) {
            this.storageId = storageId;
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

    }
}

