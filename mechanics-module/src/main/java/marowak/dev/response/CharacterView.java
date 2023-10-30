package marowak.dev.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CharacterView(
        String characterName,
        double x,
        double y,
        Integer angle,
        Float speed,
        Integer shipTypeId,
        Integer hp,
        List<Integer> polygon
) {
}
