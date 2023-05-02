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
})
@Getter
@NoArgsConstructor
@SuperBuilder
public class ItemMessage {
    private ItemMessageKey key;

    private Long id;

    private int slotId;

    private boolean equipped;

    private String characterName;

    private int itemTypeId;

    private int upgradeLevel;

    private int cost;
}
