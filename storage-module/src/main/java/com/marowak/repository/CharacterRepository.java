package com.marowak.repository;

import com.marowak.entity.Character;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;

@R2dbcRepository(dialect = Dialect.H2)
public interface CharacterRepository extends ReactiveStreamsCrudRepository<Character, String> {
    void update(@Id String id, int x, int y, int angle);
}
