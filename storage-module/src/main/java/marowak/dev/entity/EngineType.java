package marowak.dev.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@MappedEntity(value = "engine_types")
public record EngineType(
        @Id
        @Column(name = "engine_type_id")
        int id,

        @NotNull
        String name,

        long baseCost,

        int baseSpeed,

        int baseJump,

        @NotNull
        String dscRu,

        @NotNull
        String nameRu
        ) {
}
