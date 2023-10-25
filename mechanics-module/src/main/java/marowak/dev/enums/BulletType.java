package marowak.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum BulletType {
    KINETIC(1),
    ELECTRIC(2),
    THERMAL(3);

    private final int id;

    public static BulletType from(int damageTypeId) {
        return Arrays.stream(BulletType.values()).filter(type -> type.id == damageTypeId)
                .findFirst()
                .orElseThrow();
    }
}
