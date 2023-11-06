package marowak.dev.api.response.item;

import marowak.dev.dto.Point;

public record ItemInSpaceView(
        long id,
        Point coords,
        int itemTypeId,
        String dsc
) {
}
