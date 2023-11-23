package marowak.dev.dto.socket;

import marowak.dev.enums.ReceiveCommandType;

public record ReceiveSocketMessage<T>(
        ReceiveCommandType command,
        T data
) {
}
