package marowak.dev.character;


import lombok.Getter;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.item.Weapon;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.response.CharacterInfo;
import marowak.dev.response.InventoryInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private List<Item> cargo;

    private final SpaceShipBody shipBody;

    private boolean isShooting;
    // TODO make cursor point
    private int shootAngle;

    private final Map<Long, Item> itemsMap = new HashMap<>();

    public CharacterShip(String id, SpaceShipBody shipBody) {
        this.id = id;
        this.shipBody = shipBody;
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

    public void addItem(Item item) {
        if (item.getStorageId() == HOLD_STORAGE_ID) {
            cargo.add(item);
        } else if (item.getStorageId() == HULL_STORAGE_ID) {
            addToHull(item);
            item.init();
        }

        itemsMap.put(item.getId(), item);
    }

    public void updateItem(long id, int slotId, int storageId) {
        Item item = this.itemsMap.get(id);
        if (storageId == HULL_STORAGE_ID) {
            addToHull(item);
        } else if (storageId == HOLD_STORAGE_ID && item.getStorageId() == HULL_STORAGE_ID) {
            removeFromHull(item);
        }
        item.updatePosition(slotId, storageId);
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

    private void removeFromHull(Item item) {
        switch (item.getSlotId()) {
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

    public CharacterInfo getView() {
        Point coords = this.getCoord();

        return CharacterInfo.builder()
                .characterName(id)
                .x(coords.x())
                .y(coords.y())
                .angle(getAngle())
                .speed(getSpeed())
                .shipTypeId(hull.getTypeId())
                .hp(hull.getHp())
                .build();
    }

    // TODO
    public BodyInfo getShortView() {
        Point coords = this.getCoord();

        return BodyInfo.builder()
                .id(getId())
                .x(coords.x())
                .y(coords.y())
                .angle(getAngle())
                .speed(getSpeed())
                .build();
    }

    public InventoryInfo getInventoryView() {
        // TODO
        var items = new ArrayList<>(cargo);
        items.add(hull);
        items.add(engine);
        items.add(weapon1);
        items.add(weapon2);
        items.add(weapon3);
        items.add(weapon4);
        items.add(weapon5);

        return InventoryInfo.builder()
                .items(items)
                .config(hull.getConfig())
                .build();
    }

    public void updateShootingState(CharacterShootingRequest request) {
        this.isShooting = request.isShooting();
        this.shootAngle = request.angle();
    }

    public void updateShipPosition(CharacterMotionRequest request) {
        shipBody.updatePosition(request.speed(), request.angle(), request.forceTypeId());
    }

}
