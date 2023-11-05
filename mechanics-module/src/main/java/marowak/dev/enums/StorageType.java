package marowak.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StorageType {
    STORAGE_TYPE_HULL(1),
    STORAGE_TYPE_HOLD(2),
    STORAGE_TYPE_SPACE(3);

    private final int storageId;
}
