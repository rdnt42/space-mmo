package message;

import lombok.Builder;

@Builder
public record EngineMessage(
        Long id,

        int slotId,

        boolean equipped,

        String characterName,

        int engineTypeId,

        int speed,

        int upgradeLevel,

        int cost
) implements EquipmentMark {
}

