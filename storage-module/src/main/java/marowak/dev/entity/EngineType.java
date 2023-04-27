package marowak.dev.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "engine_types")
public class EngineType {
    @Id
    @Column(name = "engine_type_id")
    int id;

    @NotNull
    String name;

    long baseCost;

    int baseSpeed;

    int baseJump;

    @NotNull
    String dscRu;

    @NotNull
    String nameRu;
}
