package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "engines")
public class Engine extends Equipment {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "character_name", insertable = false, updatable = false)
    Character character;

    String characterName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_type_id", insertable = false, updatable = false)
    EngineType engineType;

    int engineTypeId;

    int speed;

    int upgradeLevel;

    int cost;
}

