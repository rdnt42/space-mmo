package marowak.dev.response.player;

import marowak.dev.enums.MessageCommand;

public record PlayerLeavingResponse(
        MessageCommand command,
        String playerName
) {
}
