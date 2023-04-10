package marowak.dev.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Character;
import org.reactivestreams.Publisher;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface CharacterRepository extends ReactiveStreamsCrudRepository<Character, String> {
    void update(@Id String id, long x, long y, int angle);

    Publisher<Character> findByOnline(boolean isOnline);

    void update(@Id String id, boolean online);
}
