package marowak.dev.character;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Item {
    private long id;

    public void init() {
        // base init method
    }
}
