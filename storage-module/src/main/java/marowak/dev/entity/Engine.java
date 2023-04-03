package marowak.dev.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@MappedEntity(value = "engines")
public record Engine(

        @Id
        @GeneratedValue
        @Column(name = "engine_id")
        Long id,

        @Column(name = "is_active")
        boolean active,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "character_name", insertable = false, updatable = false)
        Character character,

        String characterName,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "engine_type_id", insertable = false, updatable = false)
        EngineType engineType,

        int engineTypeId,

        int speed,

        int upgradeLevel,

        int cost
) {
}
