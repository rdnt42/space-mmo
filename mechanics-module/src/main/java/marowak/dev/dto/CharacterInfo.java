package marowak.dev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CharacterInfo(
        String characterName,
        double x,
        double y,
        Integer angle,
        Float speed,
        Integer shipTypeId,
        Integer hp,
        Integer evasion,
        Integer armor
) {
}
