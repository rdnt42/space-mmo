package marowak.dev.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@MappedEntity(value = "engines")
public record Engine(

        @Id
        @GeneratedValue
        @Column(name = "engine_id")
        Long id,

        @Column(name = "is_active")
        boolean active,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        Character character,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        EngineType engineType,

        int speed,

        int upgradeLevel,

        int cost
) {
}
