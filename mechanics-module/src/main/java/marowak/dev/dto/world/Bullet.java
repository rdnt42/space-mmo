package marowak.dev.dto.world;

import lombok.Builder;

@Builder
public record Bullet(
        double x,
        double y,
        double angle
) {
}
