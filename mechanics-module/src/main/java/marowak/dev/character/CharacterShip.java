package marowak.dev.character;


import lombok.Getter;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.item.Weapon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static marowak.dev.service.item.ItemServiceImpl.HOLD_STORAGE_ID;
import static marowak.dev.service.item.ItemServiceImpl.HULL_STORAGE_ID;

@Getter
public class CharacterShip {
    private String id;
    private double x;
    private double y;
    private int angle;
    private float speed;
    private Engine engine;
    private Hull hull;
    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon weapon3;
    private Weapon weapon4;
    private Weapon weapon5;

    private List<Item> cargo;

    private final Map<Long, Item> itemsMap = new HashMap<>();

    public CharacterShip(String id) {
        this.id = id;
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

    public void updateCoords(double x, double y) {
        this.x = x;
        this.y = y;
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
        switch (item.getSlotId()) {
            case 1 -> engine = item;
            case 8 -> hull = item;
            case 9 -> weapon1 = item;
            case 10 -> weapon2 = item;
            case 11 -> weapon3 = item;
            case 12 -> weapon4 = item;
            case 13 -> weapon5 = item;
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
}
