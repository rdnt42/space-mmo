package marowak.dev.dto.ship;

import marowak.dev.dto.Point;

public record ShipCreateRequest(String id, Point coords, int angle, int shipTypeId) {
}
