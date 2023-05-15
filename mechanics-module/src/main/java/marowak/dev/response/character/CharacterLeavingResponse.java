package marowak.dev.response.character;

import marowak.dev.enums.MessageCommand;

public record CharacterLeavingResponse(
        MessageCommand command,
        String playerName
) {
}
