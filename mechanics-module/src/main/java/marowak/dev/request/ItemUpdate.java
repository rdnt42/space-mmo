package marowak.dev.request;

import lombok.Builder;

@Builder
public record ItemUpdate(
        long id,
        int slotId,
        int storageId
) {
}
