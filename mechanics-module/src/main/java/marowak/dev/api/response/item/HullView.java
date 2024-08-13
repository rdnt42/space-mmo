package marowak.dev.api.response.item;

import lombok.Getter;

@Getter
public class HullView extends ItemView {
    private final int hp;
    private final int evasion;
    private final int armor;
    private final int equipmentTypeId;
    private final int config;

    private HullView(Builder builder) {
        super(builder);
        hp = builder.hp;
        evasion = builder.evasion;
        armor = builder.armor;
        equipmentTypeId = builder.equipmentTypeId;
        config = builder.config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ItemView.Builder {
        private int hp;
        private int evasion;
        private int armor;
        private int equipmentTypeId;
        private int config;

        @Override
        public HullView build() {
            return new HullView(this);
        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }

        public Builder evasion(int evasion) {
            this.evasion = evasion;
            return this;
        }

        public Builder armor(int armor) {
            this.armor = armor;
            return this;
        }

        public Builder config(int config) {
            this.config = config;
            return this;
        }
    }
}
