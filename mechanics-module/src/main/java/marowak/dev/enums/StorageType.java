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


    public boolean equals(int storageId) {
        return this.storageId == storageId;
    }

    public static StorageType from(int storageId) {
        for (StorageType value : StorageType.values()) {
            if (value.getStorageId() == storageId) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unsupported storage type" + storageId);
    }

    public boolean isShipStorage() {
        return this == STORAGE_TYPE_HULL || this == STORAGE_TYPE_HOLD;
    }

    public boolean isSpaceStorage() {
        return this == STORAGE_TYPE_SPACE;
    }
}
