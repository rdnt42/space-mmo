package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class CargoHookView extends ItemView {
    private final int loadCapacity;
    private final int radius;
    private final int equipmentTypeId;

    private CargoHookView(Builder builder) {
        super(builder);
        loadCapacity = builder.loadCapacity;
        radius = builder.radius;
        equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemView.Builder {
        private int loadCapacity;
        private int radius;
        private int equipmentTypeId;

        @Override
        public CargoHookView build() {
            return new CargoHookView(this);
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
