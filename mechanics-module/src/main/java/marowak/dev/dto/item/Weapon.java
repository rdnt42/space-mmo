package marowak.dev.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Weapon extends Item {
    private int damage;
    private int radius;
    private int rate;
    private int damageTypeId;
    private int equipmentTypeId;

    @JsonIgnore
    private long lastShoot;

    // TODO make dto
    public boolean isReadyForShoot() {
        return System.currentTimeMillis() > (lastShoot + 60_000 / rate);
    }

    public void updateShoot() {
        lastShoot = System.currentTimeMillis();
    }
}
