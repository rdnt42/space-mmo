package marowak.dev.entity;

import io.micronaut.core.annotation.Nullable;
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

        @Nullable
        Integer slotId,

        boolean equipped,

        @Column(name = "character_name")
        String characterName,

        @Column(name = "item_type_id")
        int itemTypeId,

        Integer upgradeLevel,

        Integer cost,

        String nameRu,

        String dscRu
) {
}
