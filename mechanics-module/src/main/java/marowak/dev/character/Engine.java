package marowak.dev.character;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Engine extends Item {
    private int speed;
    private int jump;
}

