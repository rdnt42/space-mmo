package marowak.dev.response;

import lombok.Builder;
import marowak.dev.enums.BulletType;

@Builder
public record BulletBodyInfo(
        String id,
        String creatorId,
        BulletType type,
        double x,
        double y,
        int angle,
        float speed
) {
}
