package marowak.dev.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Hull;
import marowak.dev.service.item.mark.HullItemService;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface HullR2Repository extends ReactiveStreamsCrudRepository<Hull, Long>, HullItemService {
}
