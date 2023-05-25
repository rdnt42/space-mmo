package marowak.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ForceType {
    POSITIVE(1),
    NEGATIVE(-1),
    REVERSE(2),
    NEUTRAL(0);

    private final int id;
}
