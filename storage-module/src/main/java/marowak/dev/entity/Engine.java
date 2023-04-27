package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "engines")
public record Engine (
        @Id
        @GeneratedValue(generator = "equipment_seq")
        @Column(name = "equipment_id")
        Long id,

        int slotId,

        boolean equipped,
        @Column(name = "character_name")
        String characterName,

        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        @JoinColumn(name = "engine_type_id", insertable = false, updatable = false)
        EngineType engineType,

        @Column(name = "engine_type_id")
        int engineTypeId,

        int speed,

        int upgradeLevel,

        int cost
        ) implements Equipment
{
}

