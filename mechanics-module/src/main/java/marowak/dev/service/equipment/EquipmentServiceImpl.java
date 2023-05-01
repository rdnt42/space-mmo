package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.broker.EquipmentClient;
import message.EquipmentMessage;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentClient equipmentClient;
    private final CharacterInventoryService characterInventoryService;

    @Override
    public void sendGetEquipments(EquipmentMessageKey key, String characterName) {
        // TODO add other equipments type
        EquipmentMessage message = EquipmentMessage.builder()
                .key(key)
                .build();

        equipmentClient.sendGetEquipments(message)
                .doOnError(e -> log.error("Send Equipments init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send Equipments init, key: {}, character: {}", key, characterName))
                .subscribe();
    }

    @Override
    public void updateEquipment(EquipmentMessage message) {
        characterInventoryService.updateInventory(message);
    }
}
