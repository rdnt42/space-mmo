package marowak.dev.response.player;

import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.MessageCommand;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 01.12.2022
 * Time: 23:04
 */
public record PlayerMotionResponse(
        MessageCommand command,
        PlayerMotion playerMotion
        ) {
}
