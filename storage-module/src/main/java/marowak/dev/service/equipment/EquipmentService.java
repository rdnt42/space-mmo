package marowak.dev.service.equipment;

import marowak.dev.entity.Equipment;
import org.reactivestreams.Publisher;

public interface EquipmentService {
    Publisher<? extends Equipment> getAllOnline();
}
