package marowak.dev.dto.motion;

import java.util.List;

public record PlayersMotionListResponse(
        List<PlayerMotion> playerMotions
) {
}
