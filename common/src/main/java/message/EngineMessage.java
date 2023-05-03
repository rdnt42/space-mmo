package message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class EngineMessage extends ItemMessage {
    private int speed;

    private int jump;

    private int engineType;
}

