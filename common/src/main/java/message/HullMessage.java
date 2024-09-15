package message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Getter
public class HullMessage extends ItemMessage {
    private final int hp;
    private final int evasion;
    private final int armor;
    private final int equipmentTypeId;
    private final int config;

    public HullMessage(Builder builder) {
        super(builder);
        this.hp = builder.hp;
        this.evasion = builder.evasion;
        this.armor = builder.armor;
        this.equipmentTypeId = builder.equipmentTypeId;
        this.config = builder.config;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder copy() {
        Builder builder = new Builder()
                .hp(hp)
                .evasion(evasion)
                .armor(armor)
                .equipmentTypeId(equipmentTypeId)
                .config(config);
        setDataFromParent(builder);

        return builder;
    }

    public static class Builder extends ItemMessage.Builder {
        private int hp;
        private int evasion;
        private int armor;
        private int equipmentTypeId;
        private int config;

        public HullMessage build() {
            return new HullMessage(this);
        }

        public Builder hp(int hp) {
            this.hp = hp;
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

        public Builder equipmentTypeId(int equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
            return this;
        }

        public Builder config(int config) {
            this.config = config;
            return this;
        }
    }
}
