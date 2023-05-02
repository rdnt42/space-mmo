package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "items")
public record Item(
        @Id
        @Column(name = "item_id")
        Long id,

        int slotId,

        boolean equipped,

        @Column(name = "character_name")
        String characterName,

        @Column(name = "item_type_id")
        int itemTypeId,

        int upgradeLevel,

        int cost
) {
}
