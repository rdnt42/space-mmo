package marowak.dev.service.equipment;

import reactor.core.publisher.Flux;

public interface EquipmentService {
    Flux<String> getAllOnline();

    Flux<String> getForCharacter(String characterName);
}
