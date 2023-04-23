package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import marowak.dev.dto.PlayerInventory;
import marowak.dev.dto.equipment.Cargo;
import marowak.dev.dto.equipment.Engine;
import marowak.dev.dto.equipment.Equipment;
import marowak.dev.dto.equipment.FuelTank;
import marowak.dev.response.player.PlayerInventoryResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class PlayerInventoryServiceImpl implements PlayerInventoryService {
    private final Map<String, PlayerInventory> playerInventoryMap = new ConcurrentHashMap<>();

    @Override
    public PlayerInventoryResponse getPlayerInventory(String playerName) {
        PlayerInventory playerInventory = playerInventoryMap.get(playerName);
        if (playerInventory == null) {
            return null;
        }

        return PlayerInventoryResponse.builder()
                .slots(playerInventory.slots())
                .cargos(playerInventory.cargos())
                .equipments(playerInventory.equipments())
                .build();
    }


    public PlayerInventoryServiceImpl() {
        Equipment engine = Engine.builder()
                .id(1)
                .slotId(1)
                .engineTypeId(1)
                .speed(600)
                .upgradeLevel(0)
                .cost(10000)
                .equipped(true)
                .build();

        Equipment engineNonEquipped = Engine.builder()
                .id(2)
                .slotId(1)
                .engineTypeId(2)
                .speed(800)
                .upgradeLevel(0)
                .cost(100000)
                .equipped(false)
                .build();

        Equipment fuelTank = FuelTank.builder()
                .id(3)
                .slotId(2)
                .fuelTankTypeId(1)
                .build();

        Collection<Integer> slots = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            slots.add(i);
        }

        Cargo cargo = new Cargo(engineNonEquipped.getId(), 0);

        PlayerInventory inventory = PlayerInventory.builder()
                .cargos(Collections.singletonList(cargo))
                .equipments(Arrays.asList(engine, engineNonEquipped, fuelTank))
                .slots(slots)
                .build();

        playerInventoryMap.put("p1", inventory);
    }
}
