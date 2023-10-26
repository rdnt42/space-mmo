package marowak.dev.dto.bullet;

import marowak.dev.dto.Point;

public record BulletCreateRequest(double angle, Point coords, Point impulse, String creatorId, int damage) {
}
