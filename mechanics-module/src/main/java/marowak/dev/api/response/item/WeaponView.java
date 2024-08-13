package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class WeaponView extends ItemView {
    private final int damage;
    private final int radius;
    private final int rate;
    private final int damageTypeId;
    private final int equipmentTypeId;

    private WeaponView(Builder builder) {
        super(builder);
        damage = builder.damage;
        radius = builder.radius;
        rate = builder.rate;
        damageTypeId = builder.damageTypeId;
        equipmentTypeId = builder.equipmentTypeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemView.Builder {
        private int damage;
        private int radius;
        private int rate;
        private int damageTypeId;
        private int equipmentTypeId;

        @Override
        public WeaponView build() {
            return new WeaponView(this);
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
