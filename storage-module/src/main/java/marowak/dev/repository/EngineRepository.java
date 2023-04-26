package marowak.dev.repository;

import io.micronaut.data.repository.CrudRepository;
import marowak.dev.entity.Engine;

public interface EngineRepository extends CrudRepository<Engine, Long> {
}
