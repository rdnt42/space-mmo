package marowak.dev.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Item;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface ItemR2Repository extends ReactiveStreamsCrudRepository<Item, Long> {
    void update(@Id long id, @NotNull Integer slotId, @NotNull Integer storageId);

    Flux<Item> findByCharacterName(@NotNull String characterName);
}
