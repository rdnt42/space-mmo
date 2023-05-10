package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "cargo_hooks")
public record CargoHook(
        @Id
        @Column(name = "item_id")
        Long id,

        int loadCapacity,

        int radius,

        int cargoHookTypeId
) {
}

