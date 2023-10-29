package marowak.dev.dto.config;

import marowak.dev.dto.Point;

import java.util.List;

public record ShipConfig(List<Point> polygon, float scale) {
}
