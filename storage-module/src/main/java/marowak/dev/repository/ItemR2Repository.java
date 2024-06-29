package marowak.dev.repository;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.constraints.NotNull;
import marowak.dev.entity.Item;
import reactor.core.publisher.Flux;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface ItemR2Repository extends ReactiveStreamsCrudRepository<Item, Long> {
    void update(@Id long id, @NotNull Integer slotId, @NotNull Integer storageId, @Nullable String characterName,
                @Nullable Double x, @Nullable Double y);

    Flux<Item> findByCharacterName(@NotNull String characterName);

    Flux<Item> findByStorageId(@NotNull int storageId);
}
