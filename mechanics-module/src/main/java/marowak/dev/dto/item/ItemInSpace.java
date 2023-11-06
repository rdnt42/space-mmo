package marowak.dev.dto.item;

import marowak.dev.dto.Point;

public record ItemInSpace(long id, Point coords, int itemTypeId, String dsc) {
}
