package marowak.dev.dto.motion;

import java.util.List;

public record PlayerMotionListResponse(
        Motion motion,
        List<PlayerMotion> playerMotions
) {
}
