package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Equipment;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.EngineRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineRepository engineRepository;
    private final EngineR2Repository engineR2Repository;

    @Override
    public List<Equipment> getAllOnline() {
        return StreamSupport
                .stream(engineRepository.findAll().spliterator(), false)
                .toList();
    }

}
