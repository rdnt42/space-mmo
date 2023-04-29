package marowak.dev.service.equipment;

import keys.EquipmentMessageKey;

public interface EquipmentService {
    void sendGetEquipments(EquipmentMessageKey key, String characterName);

}
