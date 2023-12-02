package marowak.dev.api.request;

import lombok.Builder;

@Builder
public record ItemUpdateRequest(
        long id,
        int slotId,
        int storageId
) {
}
