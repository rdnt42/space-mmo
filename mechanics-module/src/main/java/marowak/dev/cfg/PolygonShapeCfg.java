package marowak.dev.cfg;

import org.dyn4j.geometry.Vector2;
import org.jbox2d.common.Vec2;

public class PolygonShapeCfg {

    public static Vec2[] getPolygons(int shipTypeId) {

        if (shipTypeId == 1) {
            return new Vec2[]{
                    new Vec2(127, 61),
                    new Vec2(111, 22),
                    new Vec2(69, 10),
                    new Vec2(5, 13),
                    new Vec2(7, 119),
                    new Vec2(81, 116),
                    new Vec2(119, 98),

            };
        } else if (shipTypeId == 2) {
            return new Vec2[]{
                    new Vec2(127, 61),
                    new Vec2(119, 26),
                    new Vec2(45, 1),
                    new Vec2(23, 9),
                    new Vec2(8, 48),
                    new Vec2(9, 82),
                    new Vec2(19, 112),
                    new Vec2(44, 126),
                    new Vec2(119, 99),
            };
        } else {
            throw new IllegalArgumentException("");
        }
    }

    public static Vector2[] getPolygonsDyn(int shipTypeId) {

        if (shipTypeId == 1) {
            return new Vector2[]{
                    new Vector2(10, 16),
                    new Vector2(125, 62),
                    new Vector2(12, 114),
            };
        } else if (shipTypeId == 2) {
            return new Vector2[]{
                    new Vector2(127, 61),
                    new Vector2(119, 26),
                    new Vector2(45, 1),
                    new Vector2(23, 9),
                    new Vector2(8, 48),
                    new Vector2(9, 82),
                    new Vector2(19, 112),
                    new Vector2(44, 126),
                    new Vector2(119, 99),
            };
        } else {
            throw new IllegalArgumentException("");
        }
    }
}
