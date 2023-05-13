package marowak.dev.request;

import lombok.Builder;

@Builder
public record ItemUpdate(
        long id,
        Integer slotId
) {
}
