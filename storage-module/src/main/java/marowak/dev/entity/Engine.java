package marowak.dev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "engines")
public class Engine extends Equipment {
    @Column(name = "character_name")
    String characterName;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "engine_type_id", insertable = false, updatable = false)
    EngineType engineType;

    @Column(name = "engine_type_id")
    int engineTypeId;

    int speed;

    int upgradeLevel;

    int cost;
}

