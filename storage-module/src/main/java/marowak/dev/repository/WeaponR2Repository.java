package marowak.dev.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Weapon;
import marowak.dev.service.item.mark.WeaponItemService;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface WeaponR2Repository extends ReactiveStreamsCrudRepository<Weapon, Long>, WeaponItemService {
}
