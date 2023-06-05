package marowak.dev.dto.world;

import lombok.Builder;

@Builder
public record Bullet(
        long id,
        double x,
        double y,
        double angle
) {
}
