package marowak.dev.api.response.item;

import lombok.Builder;
import marowak.dev.dto.Point;

@Builder
public record ItemInSpaceView(
        long id,
        double x,
        double y,
        int itemTypeId,
        String name,
        String dsc
) {
    public Point coords() {
        return new Point(x, y);
    }
}
