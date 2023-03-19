package marowak.dev.response.player;

import marowak.dev.dto.motion.PlayerMotion;

import java.util.Collection;

public record PlayersMotionListResponse(
        PlayerMotion playerMotion,
        Collection<PlayerMotion> playersMotions
) {
}
