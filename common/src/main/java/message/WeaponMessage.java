package message;

import lombok.Getter;

@Getter
public class WeaponMessage extends ItemMessage {
    private final int damage;
    private final int radius;
    private final int rate;
    private final int damageTypeId;
    private final int equipmentTypeId;

    public WeaponMessage(Builder builder) {
        super(builder);
        this.damage = builder.damage;
        this.radius = builder.radius;
        this.rate = builder.rate;
        this.damageTypeId = builder.damageTypeId;
        this.equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder copy() {
        Builder builder = new Builder()
                .damage(damage)
                .radius(radius)
                .rate(rate)
                .damageTypeId(damageTypeId)
                .equipmentTypeId(equipmentTypeId);
        setDataFromParent(builder);

        return builder;
    }

    public static class Builder extends ItemMessage.Builder {
        private int damage;
        private int radius;
        private int rate;
        private int damageTypeId;
        private int equipmentTypeId;

        @Override
        public WeaponMessage build() {
            return new WeaponMessage(this);
        }

        public Builder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }

        public Builder damageTypeId(int damageTypeId) {
            this.damageTypeId = damageTypeId;
            return this;
        }

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }
    }
}
