package message;

import lombok.Getter;

@Getter
public class CargoHookMessage extends ItemMessage {
    private final int loadCapacity;
    private final int radius;
    private final int equipmentTypeId;

    public CargoHookMessage(Builder builder) {
        super(builder);
        this.loadCapacity = builder.loadCapacity;
        this.radius = builder.radius;
        this.equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemMessage.Builder {
        private int loadCapacity;
        private int radius;
        private int equipmentTypeId;

        public CargoHookMessage build() {
            return new CargoHookMessage(this);
        }

        public Builder loadCapacity(int loadCapacity) {
            this.loadCapacity = loadCapacity;
            return this;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }
    }
}
