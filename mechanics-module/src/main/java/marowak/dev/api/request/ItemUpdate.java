package marowak.dev.api.request;

import lombok.Builder;

@Builder
public record ItemUpdate(
        long id,
        Integer slotId,
        int storageId
) {
}
