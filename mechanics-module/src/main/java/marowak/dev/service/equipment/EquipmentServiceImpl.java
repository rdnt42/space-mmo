package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.EquipmentMessageKey;
import marowak.dev.service.broker.EquipmentClient;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentClient equipmentClient;

    @Override
    public void sendGetEquipments(EquipmentMessageKey key, String characterName) {
        equipmentClient.sendGetEquipments(key, characterName)
                .doOnError(e -> log.error("Send Characters init error, error: {}", e.getMessage()))
                .doOnSuccess(c -> log.info("Send Character init successful"))
                .subscribe();
    }
}
