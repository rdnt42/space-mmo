package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class EngineView extends ItemView {
    private final int speed;
    private final int jump;
    private final int equipmentTypeId;

    private EngineView(Builder builder) {
        super(builder);
        speed = builder.speed;
        jump = builder.jump;
        equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemView.Builder {
        private int speed;
        private int jump;
        private int equipmentTypeId;

        @Override
        public EngineView build() {
            return new EngineView(this);
        }

        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder jump(int jump) {
            this.jump = jump;
            return this;
        }

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }
    }
}
