package message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import keys.ItemMessageKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EngineMessage.class, name = "engine"),
        @JsonSubTypes.Type(value = FuelTankMessage.class, name = "fuel_tank"),
        @JsonSubTypes.Type(value = CargoHookMessage.class, name = "cargo_hook"),
})
@Getter
@NoArgsConstructor
@SuperBuilder
public class ItemMessage {
    private ItemMessageKey key;

    private Long id;

    private Integer slotId;

    private String characterName;

    private int typeId;

    private Integer upgradeLevel;

    private Integer cost;

    private String name;

    private String dsc;
}
