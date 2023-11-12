package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.api.response.item.WeaponView;
import marowak.dev.dto.Point;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.enums.BulletType;
import marowak.dev.service.physic.FactoryUtils;

import static marowak.dev.enums.StorageType.STORAGE_TYPE_HULL;


@Slf4j
@Getter
@NoArgsConstructor
@SuperBuilder
public class Weapon extends Item {
    private int damage;
    private int radius;
    private int rate;
    private int damageTypeId;
    private int equipmentTypeId;

    // internal
    private long lastShoot;
    private int shotFreq;
    private double shiftAngle;
    private double shiftLength;
    private Point coords;

    @Override
    public void init() {
        shotFreq = 60_000 / rate;
        if (!STORAGE_TYPE_HULL.equals(getStorageId())) return;

        var slotShift = switch (getSlotId()) {
            case 9 -> new Point(20, -20);
            case 10 -> new Point(20, 20);
            case 11 -> new Point(-32, -32);
            case 12 -> new Point(-32, 0);
            case 13 -> new Point(-32, 32);
            default -> new Point(0, 0);
        };

        shiftLength = Math.sqrt(slotShift.x() * slotShift.x() + slotShift.y() * slotShift.y());
        shiftAngle = Math.toDegrees(Math.atan(slotShift.y() / slotShift.x()));
        if (getSlotId() >= 11) shiftAngle -= 180;
        log.info("change weapon slot: {}, angle: {}, length: {}", getSlotId(), shiftAngle, shiftLength);
    }

    @Override
    public ItemView getView() {
        WeaponView.WeaponViewBuilder<?, ?> builder = WeaponView.builder()
                .damage(damage)
                .radius(radius)
                .rate(rate)
                .damageTypeId(damageTypeId)
                .equipmentTypeId(equipmentTypeId);

        return super.getItemBuilder(builder)
                .build();
    }

    public boolean isReadyForShoot() {
        return System.currentTimeMillis() > (lastShoot + shotFreq);
    }

    public void updateCoords(Point baseCoords, double baseAngle) {
        var angleInRadians = Math.toRadians(baseAngle + shiftAngle);
        var newX = shiftLength * Math.cos(angleInRadians);
        var newY = shiftLength * Math.sin(angleInRadians);
        coords = new Point(baseCoords.x() + newX, baseCoords.y() + newY);
    }

    @Override
    public void updateStorage(int slotId, int storageId) {
        super.updateStorage(slotId, storageId);
        init();
    }

    public BulletBody makeShootRequest(String creatorId, double angle, Point impulse) {
        var angleInRadians = Math.toRadians(angle);
        var request = new BulletCreateRequest(angleInRadians, this.coords, impulse, creatorId, this.damage);
        lastShoot = System.currentTimeMillis();

        return switch (BulletType.from(damageTypeId)) {
            case KINETIC -> FactoryUtils.createKineticBullet(request);
            case ELECTRIC -> FactoryUtils.createElectricBullet(request);
            case THERMAL -> FactoryUtils.createThermalBullet(request);
        };
    }

}
