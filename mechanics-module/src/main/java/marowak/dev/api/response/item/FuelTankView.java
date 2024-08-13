package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class FuelTankView extends ItemView {
    private final int capacity;
    private final int equipmentTypeId;

    private FuelTankView(Builder builder) {
        super(builder);
        capacity = builder.capacity;
        equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemView.Builder {
        private int capacity;
        private int equipmentTypeId;

        @Override
        public FuelTankView build() {
            return new FuelTankView(this);
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
