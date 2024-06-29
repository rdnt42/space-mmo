package marowak.dev.entity;

import jakarta.validation.constraints.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "engine_types")
public record EngineType (
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
)
{
}
