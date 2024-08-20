package message;

import lombok.Getter;

@Getter
public class FuelTankMessage extends ItemMessage {
    private final int capacity;
    private final int equipmentTypeId;

    public FuelTankMessage(Builder builder) {
        super(builder);
        this.equipmentTypeId = builder.equipmentTypeId;
        this.capacity = builder.capacity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemMessage.Builder {
        private int capacity;
        private int equipmentTypeId;

        public FuelTankMessage build() {
            return new FuelTankMessage(this);
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }
    }
}

