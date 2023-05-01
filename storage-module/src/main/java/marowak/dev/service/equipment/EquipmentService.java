package marowak.dev.service.equipment;

import message.EquipmentMessage;
import reactor.core.publisher.Flux;

public interface EquipmentService {
    Flux<EquipmentMessage> getAllOnline();
}
