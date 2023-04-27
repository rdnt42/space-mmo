package marowak.dev.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import marowak.dev.entity.Equipment;

@Repository
//public interface EngineRepository<T extends Equipment, E extends Serializable> extends CrudRepository<T, E> {
public interface EngineRepository extends CrudRepository<Equipment, Long> {
}
