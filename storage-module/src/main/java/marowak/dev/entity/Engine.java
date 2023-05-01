package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "engines")
public record Engine(
        @Id
        @Column(name = "equipment_id")
        Long id,
        int speed,

        int cost
) {
}

