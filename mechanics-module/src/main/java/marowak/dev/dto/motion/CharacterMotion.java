package marowak.dev.dto.motion;

import lombok.Builder;

@Builder
public record CharacterMotion(
        String characterName,
        double x,
        double y,
        int angle,
        float speed
) {
}
