package marowak.dev.service.equipment;

import message.EquipmentMark;
import reactor.core.publisher.Flux;

public interface EquipmentService {
    Flux<? extends EquipmentMark> getAllOnline();

    Flux<? extends EquipmentMark> getForCharacter(String characterName);
}
