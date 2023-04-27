package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Equipment;
import marowak.dev.repository.EngineR2Repository;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineR2Repository engineR2Repository;

    @Override
    public Publisher<? extends Equipment> getAllOnline() {
        return engineR2Repository.findAll();
    }

}
