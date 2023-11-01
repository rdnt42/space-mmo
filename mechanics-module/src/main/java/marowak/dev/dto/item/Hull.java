package marowak.dev.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import marowak.dev.dto.Point;
import marowak.dev.dto.bullet.DamageCreator;
import marowak.dev.dto.calculate.CalculateShipDamageResult;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.response.item.HullView;
import marowak.dev.response.item.ItemView;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Hull extends Item {
    private int hp;
    private int evasion;
    private int armor;
    private int equipmentTypeId;
    private int config;

    @Setter
    private SpaceShipBody shipBody;

    @Setter
    private int currHp;

    @Override
    public ItemView getView() {
        HullView.HullViewBuilder<?, ?> builder = HullView.builder()
                .hp(hp)
                .evasion(evasion)
                .armor(armor)
                .equipmentTypeId(equipmentTypeId)
                .config(config);

        return super.getItemBuilder(builder)
                .build();
    }

    @Override
    public void init() {
        currHp = hp;
    }

    public Point getCoords() {
        return shipBody.getCoords();
    }

    public int getAngle() {
        return shipBody.getAngle();
    }

    public float getSpeed() {
        return shipBody.getSpeed();
    }

    public Point getImpulse() {
        Vector2 linearVelocity = shipBody.getLinearVelocity();

        return new Point(linearVelocity.x, linearVelocity.y);
    }

    public boolean isInRange(Hull hull) {
        return shipBody.isInRange(hull.shipBody);
    }

    public List<Integer> getPolygonCoords() {
        Polygon shape = (Polygon) shipBody.getFixture(0).getShape();
        Vector2 center = shape.getCenter();

        return Arrays.stream(shape.getVertices())
                .flatMap(point -> Stream.of((int) (point.x - center.x), (int) (point.y - center.y)))
                .toList();
    }

    public void updatePosition(int speed, int angle, int forceTypeId) {
        shipBody.updatePosition(speed, angle, forceTypeId);
    }

    public CalculateShipDamageResult calculateDamage() {
        var damageQueue = shipBody.getAccumulatedDamage();
        if (damageQueue.isEmpty()) return null;

        while (!damageQueue.isEmpty() && currHp > 0) {
            DamageCreator damageCreator = damageQueue.poll();
            currHp -= damageCreator.damage();
            if (currHp <= 0) {
                return new CalculateShipDamageResult(true, damageCreator.creatorId());
            }
        }

        return null;
    }
}
