package marowak.dev.dto.socket;

import marowak.dev.enums.SendCommandType;

public record SendSocketMessage<T>(
        SendCommandType command,
        T data
) {
}
