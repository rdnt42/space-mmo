package marowak.dev.api.response.item;

import lombok.Builder;
import marowak.dev.dto.Point;

@Builder
public record ItemInSpaceView(
        long id,
        Point coords,
        int itemTypeId,
        String name,
        String dsc
) {
}
