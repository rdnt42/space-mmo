package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "weapons")
public record Weapon(
        @Id
        @Column(name = "item_id")
        Long id,

        int damage,

        int radius,

        int rate,

        int damageTypeId,

        int weaponTypeId
) {
}
