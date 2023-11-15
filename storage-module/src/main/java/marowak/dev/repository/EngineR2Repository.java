package marowak.dev.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Engine;
import marowak.dev.service.item.mark.EngineItemService;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface EngineR2Repository extends ReactiveStreamsCrudRepository<Engine, Long>, EngineItemService {
}
