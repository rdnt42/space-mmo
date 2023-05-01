package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.equipment.Equipment;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.EquipmentMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Singleton
public class CharacterInventoryServiceImpl implements CharacterInventoryService {
    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();

    @Override
    public CharacterInventoryResponse getInventory(String playerName) {
        CharacterInventory characterInventory = playerInventoryMap.get(playerName);
        if (characterInventory == null) {
            return null;
        }

        return CharacterInventoryResponse.builder()
                .slots(characterInventory.slots())
                .cargos(characterInventory.cargos().values())
                .equipments(characterInventory.equipments().values())
                .build();
    }

    @Override
    public void updateInventory(EquipmentMessage message) {
        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());

        Equipment equipment = messageToEquipment.apply(message);
        inventory.equipments().put(equipment.getId(), equipment);
        inventory.slots().add(equipment.getSlotId());

        log.info("Inventory update successful, character name: {}, equipment id: {}", message.getCharacterName(), equipment.getId());
    }

    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .slots(new HashSet<>())
                .cargos(new HashMap<>())
                .equipments(new HashMap<>())
                .build();
    }

    private final Function<EquipmentMessage, Equipment> messageToEquipment = message -> Equipment.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .equipped(message.isEquipped())
            .equipmentType(message.getEquipmentTypeId())
            .build();
}
