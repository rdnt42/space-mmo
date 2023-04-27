package marowak.dev.repository;

import io.micronaut.data.repository.CrudRepository;
import marowak.dev.entity.Equipment;

public interface EquipmentRepository extends CrudRepository<Equipment, Long> {

}
