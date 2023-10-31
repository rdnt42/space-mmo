package marowak.dev.character;


import lombok.Getter;
import lombok.Setter;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.item.Weapon;
import marowak.dev.dto.ship.ShipCreateRequest;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.CharacterView;
import marowak.dev.response.InventoryView;
import marowak.dev.response.item.ItemView;
import marowak.dev.service.physic.FactoryUtils;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
public class CharacterShip {
    public static final int HULL_STORAGE_ID = 1;
    public static final int HOLD_STORAGE_ID = 2;
    private final String id;
    private Engine engine;
    private Hull hull;
    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon weapon3;
    private Weapon weapon4;
    private Weapon weapon5;

    private final List<Item> cargo = new ArrayList<>();

    // TODO refactor
    private final Point startCoords;
    private final int startAngle;
    @Setter
    private SpaceShipBody shipBody;

    private boolean isShooting;
    // TODO make cursor point
    private int shootAngle;

    private final Map<Long, Item> itemsMap = new HashMap<>();

    public CharacterShip(String id, Point startCoords, int startAngle) {
        this.id = id;
        this.startCoords = startCoords;
        this.startAngle = startAngle;
    }

    private Point getCoord() {
        return shipBody.getCoords();
    }

    private int getAngle() {
        return shipBody.getAngle();
    }

    private float getSpeed() {
        return shipBody.getSpeed();
    }

    private Point getImpulse() {
        Vector2 linearVelocity = shipBody.getLinearVelocity();

        return new Point(linearVelocity.x, linearVelocity.y);
    }

    public void addItem(Item item) {
        if (item.getStorageId() == HOLD_STORAGE_ID) {
            cargo.add(item);
        } else if (item.getStorageId() == HULL_STORAGE_ID) {
            addToHull(item);
            item.init();
        }

        itemsMap.put(item.getId(), item);
    }

    public SpaceShipBody addShipBody(int shipTypeId) {
        ShipCreateRequest request = new ShipCreateRequest(id, startCoords, startAngle, shipTypeId);

        return FactoryUtils.createShip(request);
    }

    public Item updateItem(long id, int slotId, int storageId) {
        Item item = this.itemsMap.get(id);
        var prevItemStorage = item.getStorageId();
        var prevSlot = item.getSlotId();
        item.updatePosition(slotId, storageId);
        if (storageId == HULL_STORAGE_ID) {
            addToHull(item);
        } else if (storageId == HOLD_STORAGE_ID && prevItemStorage == HULL_STORAGE_ID) {
            removeFromHull(prevSlot);
        }

        return item;
    }

    private void addToHull(Item item) {
        switch (item) {
            case Engine e -> this.engine = e;
            case Hull h -> this.hull = h;
            case Weapon w -> {
                switch (w.getSlotId()) {
                    case 9 -> weapon1 = w;
                    case 10 -> weapon2 = w;
                    case 11 -> weapon3 = w;
                    case 12 -> weapon4 = w;
                    case 13 -> weapon5 = w;
                }
            }
            default -> throw new IllegalStateException();
        }
    }

    private void removeFromHull(int slotId) {
        switch (slotId) {
            case 1 -> engine = null;
            case 8 -> hull = null;
            case 9 -> weapon1 = null;
            case 10 -> weapon2 = null;
            case 11 -> weapon3 = null;
            case 12 -> weapon4 = null;
            case 13 -> weapon5 = null;
            default -> throw new IllegalStateException();
        }
    }

    public boolean isInRange(CharacterShip other) {
        return shipBody.isInRange(other.getShipBody());
    }

    public CharacterView getView() {
        if (shipBody == null) return null;
        Point coords = this.getCoord();

        Polygon shape = (Polygon) shipBody.getFixture(0).getShape();
        Vector2 center = shape.getCenter();
        List<Integer> polygon = Arrays.stream(shape.getVertices())
                .flatMap(point -> Stream.of((int) (point.x - center.x), (int) (point.y - center.y)))
                .toList();

        return CharacterView.builder()
                .characterName(id)
                .x(coords.x())
                .y(coords.y())
                .angle(getAngle())
                .speed(getSpeed())
                .shipTypeId(hull.getEquipmentTypeId())
                .hp(hull.getHp())
                .polygon(polygon)
                .build();
    }

    public InventoryView getInventoryView() {
        // TODO
        List<ItemView> items = cargo.stream()
                .map(Item::getView)
                .collect(Collectors.toList());
        items.add(hull == null ? null : hull.getView());
        items.add(engine == null ? null : engine.getView());
        items.add(weapon1 == null ? null : weapon1.getView());
        items.add(weapon2 == null ? null : weapon2.getView());
        items.add(weapon3 == null ? null : weapon3.getView());
        items.add(weapon4 == null ? null : weapon4.getView());
        items.add(weapon5 == null ? null : weapon5.getView());

        return InventoryView.builder()
                .items(items.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .config(hull == null ? 0 : hull.getConfig())
                .build();
    }

    public void updateShootingState(CharacterShootingRequest request) {
        this.isShooting = request.isShooting();
        this.shootAngle = request.angle();
    }

    public void updateShipPosition(CharacterMotionRequest request) {
        shipBody.updatePosition(engine.getSpeed(), request.angle(), request.forceTypeId());
        if (weapon1 != null) weapon1.updateCoords(getCoord(), getAngle());
        if (weapon2 != null) weapon2.updateCoords(getCoord(), getAngle());
        if (weapon3 != null) weapon3.updateCoords(getCoord(), getAngle());
        if (weapon4 != null) weapon4.updateCoords(getCoord(), getAngle());
        if (weapon5 != null) weapon5.updateCoords(getCoord(), getAngle());
    }

    public List<BulletBody> useWeapons() {
        if (!isShooting) return Collections.emptyList();
        // todo get weapons
        List<BulletBody> bullets = new ArrayList<>();
        Optional.ofNullable(useWeapon(weapon1, id, getShootAngle(), getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon2, id, getShootAngle(), getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon3, id, getShootAngle(), getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon4, id, getShootAngle(), getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon5, id, getShootAngle(), getImpulse())).ifPresent(bullets::add);

        return bullets;
    }

    private BulletBody useWeapon(Weapon weapon, String creatorId, double angle, Point impulse) {
        if (weapon == null) return null;
        if (weapon.isReadyForShoot()) {
            return weapon.makeShootRequest(creatorId, angle, impulse);
        }

        return null;
    }


}
