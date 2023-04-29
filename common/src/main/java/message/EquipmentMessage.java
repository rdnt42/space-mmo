package message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
public abstract class EquipmentMessage {
    Long id;

    int slotId;

    boolean equipped;
}
