package marowak.dev.response.player;

import marowak.dev.dto.motion.Motion;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.MessageCommand;

import java.util.List;

public record PlayersMotionListResponse(
        MessageCommand command,
        Motion motion,
        List<PlayerMotion> playerMotions
) {
}
