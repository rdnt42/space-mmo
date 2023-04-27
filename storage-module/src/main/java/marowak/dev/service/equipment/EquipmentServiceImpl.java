package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Equipment;
import marowak.dev.repository.EquipmentRepository;

@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;

    @Override
    public Iterable<Equipment> getAllOnline() {
        return equipmentRepository.findAll();
    }
}
