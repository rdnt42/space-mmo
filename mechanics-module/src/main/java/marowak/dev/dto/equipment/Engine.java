package marowak.dev.dto.equipment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Engine extends Equipment {
    private final int speed;
    private final int upgradeLevel;
    private final int cost;

    @Builder
    public Engine(int id, int slotId, boolean equipped, int equipmentType, int speed, int upgradeLevel, int cost) {
        super(id, slotId, equipped, equipmentType);
        this.speed = speed;
        this.upgradeLevel = upgradeLevel;
        this.cost = cost;
    }
}
