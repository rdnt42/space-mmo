package marowak.dev.service.equipment;

import keys.EquipmentMessageKey;
import message.EquipmentMessage;

public interface EquipmentService {
    void sendGetEquipments(EquipmentMessageKey key, String characterName);

    void updateEquipment(EquipmentMessage message);
}
