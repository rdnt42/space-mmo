package marowak.dev.service.equipment;

import marowak.dev.enums.EquipmentMessageKey;

public interface EquipmentService {
    void sendGetEquipments(EquipmentMessageKey key, String characterName);

}
