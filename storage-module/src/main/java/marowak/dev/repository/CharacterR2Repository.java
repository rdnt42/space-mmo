package marowak.dev.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Character;
import reactor.core.publisher.Flux;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface CharacterR2Repository extends ReactiveStreamsCrudRepository<Character, String> {
    void update(@Id String id, double x, double y, int angle);

    Flux<Character> findByOnline(boolean isOnline);

    void update(@Id String id, boolean online);
}
