package marowak.dev.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.CargoHook;
import marowak.dev.service.item.mark.CargoHookItemService;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface CargoHookR2Repository extends ReactiveStreamsCrudRepository<CargoHook, Long>, CargoHookItemService {
}
