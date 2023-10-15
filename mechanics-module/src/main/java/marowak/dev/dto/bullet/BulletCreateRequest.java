package marowak.dev.dto.bullet;

import marowak.dev.enums.BulletType;

public record BulletCreateRequest(double angle, double x, double y, BulletType type) {
}
