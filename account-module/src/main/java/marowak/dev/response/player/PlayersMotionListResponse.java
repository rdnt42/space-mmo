package marowak.dev.response.player;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.MessageCommand;

import java.util.Collection;

public record PlayersMotionListResponse(
        MessageCommand command,
        PlayerMotion playerMotion,
        Collection<PlayerMotion> playerMotions
) {
}
