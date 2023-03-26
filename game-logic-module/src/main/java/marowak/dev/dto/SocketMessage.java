package marowak.dev.dto;

import marowak.dev.enums.MessageCommand;

public record SocketMessage<T>(
        MessageCommand command,
        T data
) {
}
