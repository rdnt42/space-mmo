package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "hulls")
public record Hull(
        @Id
        @Column(name = "item_id")
        Long id,

        @Column(name = "hp")
        int hp,

        int evasion,

        int armor,

        int hullTypeId
) {
}
