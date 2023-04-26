package marowak.dev.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "engine_types")
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
