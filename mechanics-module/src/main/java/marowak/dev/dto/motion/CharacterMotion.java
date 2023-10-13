package marowak.dev.dto.motion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import marowak.dev.dto.BodyCreator;

@Builder
public record CharacterMotion(
        String characterName,
        double x,
        double y,
        int angle,
        float speed,
        @JsonIgnore
        long lastUpdateTime
) implements BodyCreator {
}
