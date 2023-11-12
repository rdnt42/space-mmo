package marowak.dev.character;


import lombok.Getter;
import lombok.Setter;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.api.response.CharacterView;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.Point;
import marowak.dev.dto.calculate.CalculateShipDamageResult;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.item.Weapon;
import marowak.dev.dto.ship.ShipCreateRequest;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.service.physic.FactoryUtils;
import org.dyn4j.dynamics.Body;

import java.util.*;
import java.util.stream.Collectors;

import static marowak.dev.enums.StorageType.STORAGE_TYPE_HOLD;
import static marowak.dev.enums.StorageType.STORAGE_TYPE_HULL;


@Getter
public class CharacterShip {
    private final String id;
    private Engine engine;
    private Hull hull;
    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon weapon3;
    private Weapon weapon4;
    private Weapon weapon5;

    private final Item[] hold = new Item[30];

    // TODO refactor
    private final Point startCoords;
    private final int startAngle;
    @Setter

    private boolean isShooting;
    // TODO make cursor point
    private int shootAngle;

    private final Map<Long, Item> itemsMap = new HashMap<>();

    public CharacterShip(String id, Point startCoords, int startAngle) {
        this.id = id;
        this.startCoords = startCoords;
        this.startAngle = startAngle;
    }

    public Point getCoords() {
        return hull.getCoords();
    }

    private int getAngle() {
        return hull.getAngle();
    }

    private float getSpeed() {
        return hull.getSpeed();
    }

    public SpaceShipBody createShipBody() {
        ShipCreateRequest request = new ShipCreateRequest(id, startCoords, startAngle, hull.getEquipmentTypeId());
        SpaceShipBody body = FactoryUtils.createShip(request);
        hull.setShipBody(body);

        return body;
    }

    public Item updateItem(long id, int slotId, int storageId) {
        Item item = this.itemsMap.get(id);
        removeItem(item);
        item.updateStorage(slotId, storageId);
        addItem(item);

        return item;
    }

    public void addItem(Item item) {
        int storageId = item.getStorageId();
        if (STORAGE_TYPE_HOLD.equals(storageId)) {
            addToHold(item);
        } else if (STORAGE_TYPE_HULL.equals(storageId)) {
            addToHull(item);
            item.init();
        }

        itemsMap.put(item.getId(), item);
    }

    private void addToHull(Item item) {
        // TODO check if slot is busy
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

    private void addToHold(Item item) {
        hold[item.getSlotId()] = item;
    }

    private void removeItem(Item item) {
        int storageId = item.getStorageId();
        if (STORAGE_TYPE_HULL.equals(storageId)) {
            removeFromHull(item.getSlotId());
        } else if (STORAGE_TYPE_HOLD.equals(storageId)) {
            removeFromHold(item.getSlotId());
        }

        itemsMap.remove(item.getId());
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

    private void removeFromHold(int slotId) {
        hold[slotId] = null;
    }

    public boolean isInRange(CharacterShip other) {
        return hull.isInRange(other.getHull());
    }

    public CharacterView getView() {
        if (hull == null) return null;
        Point coords = this.getCoords();

        return CharacterView.builder()
                .characterName(id)
                .x(coords.x())
                .y(coords.y())
                .angle(getAngle())
                .speed(getSpeed())
                .shipTypeId(hull.getEquipmentTypeId())
                .hp(hull.getHp())
                .polygon(hull.getPolygonCoords())
                .build();
    }

    public InventoryView getInventoryView() {
        List<ItemView> items = itemsMap.values().stream()
                .map(Item::getView)
                .toList();

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
        hull.updatePosition(engine.getSpeed(), request.angle(), request.forceTypeId());
        if (weapon1 != null) weapon1.updateCoords(getCoords(), getAngle());
        if (weapon2 != null) weapon2.updateCoords(getCoords(), getAngle());
        if (weapon3 != null) weapon3.updateCoords(getCoords(), getAngle());
        if (weapon4 != null) weapon4.updateCoords(getCoords(), getAngle());
        if (weapon5 != null) weapon5.updateCoords(getCoords(), getAngle());
    }

    public List<BulletBody> useWeapons() {
        if (!isShooting) return Collections.emptyList();
        // TODO use coords from hull instead of weapon coords
        List<BulletBody> bullets = new ArrayList<>();
        Optional.ofNullable(useWeapon(weapon1, id, getShootAngle(), hull.getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon2, id, getShootAngle(), hull.getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon3, id, getShootAngle(), hull.getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon4, id, getShootAngle(), hull.getImpulse())).ifPresent(bullets::add);
        Optional.ofNullable(useWeapon(weapon5, id, getShootAngle(), hull.getImpulse())).ifPresent(bullets::add);

        return bullets;
    }

    private BulletBody useWeapon(Weapon weapon, String creatorId, double angle, Point impulse) {
        if (weapon == null) return null;
        if (weapon.isReadyForShoot()) {
            return weapon.makeShootRequest(creatorId, angle, impulse);
        }

        return null;
    }

    public CalculateShipDamageResult calculateDamage() {
        if (hull == null) return null;

        return hull.calculateDamage();
    }

    public List<Body> destroy() {
        List<Body> bodies = new ArrayList<>();
        // TODO #95
        if (hull != null) bodies.add(hull.getShipBody());

        return bodies;
    }

}
