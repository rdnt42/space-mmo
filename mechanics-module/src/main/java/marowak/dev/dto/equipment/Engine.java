package marowak.dev.dto.equipment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Engine extends Equipment {
    private final boolean equipped;
    private final int engineTypeId;
    private final int speed;
    private final int upgradeLevel;
    private final int cost;

    @Builder
    public Engine(int id, int slotId, boolean equipped, int engineTypeId, int speed, int upgradeLevel, int cost) {
        super(id, slotId);
        this.equipped = equipped;
        this.engineTypeId = engineTypeId;
        this.speed = speed;
        this.upgradeLevel = upgradeLevel;
        this.cost = cost;
    }
}
