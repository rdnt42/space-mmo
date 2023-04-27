package marowak.dev.service.equipment;

import marowak.dev.entity.Equipment;

public interface EquipmentService {
    Iterable<Equipment> getAllOnline();
}
