package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "equipments")
public record Equipment(
        @Id
        @GeneratedValue(generator = "equipment_seq")
        @Column(name = "equipment_id")
        Long id,

        int slotId,

        boolean equipped,

        @Column(name = "character_name")
        String characterName,

        @Column(name = "equipment_type_id")
        int equipmentTypeId,

        int upgradeLevel
) {
}
