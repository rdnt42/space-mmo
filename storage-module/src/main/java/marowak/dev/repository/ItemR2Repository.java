package marowak.dev.repository;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Item;
import reactor.core.publisher.Flux;

@R2dbcRepository(dialect = Dialect.POSTGRES)

public interface ItemR2Repository extends ReactiveStreamsCrudRepository<Item, Long> {
    Flux<Item> findByCharacterName(String characterName);

}
