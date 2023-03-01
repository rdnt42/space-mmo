package marowak.dev.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.entity.Character;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface CharacterRepository extends ReactiveStreamsCrudRepository<Character, String> {
    void update(@Id String id, int x, int y, int angle);
}