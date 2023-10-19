package marowak.dev.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import marowak.dev.request.CharacterShootingRequest;

import java.awt.*;

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
    @JsonIgnore
    private int shotFreq;
    @JsonIgnore
    private Point slotShift;
    @JsonIgnore
    private Point coord;
    @JsonIgnore
    private double angle;
    @JsonIgnore
    private boolean isShooting;


    @Override
    public void init() {
        shotFreq = 60_000 / rate;
        slotShift = switch (getSlotId()) {
            case 9 -> new Point(-20, -20);
            case 10 -> new Point(20, -20);
            case 11 -> new Point(-32, 20);
            case 12 -> new Point(0, 20);
            case 13 -> new Point(32, 20);
            default -> new Point(0, 0);
        };
    }

    public boolean isReadyForShoot() {
        return System.currentTimeMillis() > (lastShoot + shotFreq);
    }

    public void updateShoot() {
        lastShoot = System.currentTimeMillis();
    }

    public void changeShootState(CharacterShootingRequest request, Point base) {
        isShooting = request.isShooting();
        angle = request.angle();
    }

}
