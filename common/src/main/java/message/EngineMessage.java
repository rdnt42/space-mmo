package message;

import lombok.Getter;

@Getter
public class EngineMessage extends ItemMessage {
    private final int speed;
    private final int jump;
    private final int equipmentTypeId;

    public EngineMessage(Builder builder) {
        super(builder);
        this.speed = builder.speed;
        this.jump = builder.jump;
        this.equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder copy() {
        Builder builder = new Builder()
                .speed(speed)
                .jump(jump)
                .equipmentTypeId(equipmentTypeId);
        setDataFromParent(builder);

        return builder;
    }

    public static class Builder extends ItemMessage.Builder {
        private int speed;
        private int jump;
        private int equipmentTypeId;

        @Override
        public EngineMessage build() {
            return new EngineMessage(this);
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

