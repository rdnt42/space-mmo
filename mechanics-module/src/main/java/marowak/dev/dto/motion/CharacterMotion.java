package marowak.dev.dto.motion;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record CharacterMotion(
        String playerName,
        double x,
        double y,
        int angle,
        float speed,
        @JsonIgnore
        long lastUpdateTime
) {
}
