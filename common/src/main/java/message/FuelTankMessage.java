package message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
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

    @Override
    public Builder copy() {
        Builder builder = new Builder()
                .capacity(capacity)
                .equipmentTypeId(equipmentTypeId);
        setDataFromParent(builder);

        return builder;
    }

    public static class Builder extends ItemMessage.Builder {
        private int capacity;
        private int equipmentTypeId;

        @Override
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

