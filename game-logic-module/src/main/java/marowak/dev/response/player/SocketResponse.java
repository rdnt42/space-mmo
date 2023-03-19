package marowak.dev.response.player;

import marowak.dev.enums.MessageCommand;

public record SocketResponse<T>(
        MessageCommand command,
        T data
) {
}
