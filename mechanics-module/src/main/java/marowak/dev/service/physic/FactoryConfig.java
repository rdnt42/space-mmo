package marowak.dev.service.physic;

import marowak.dev.dto.Point;
import marowak.dev.dto.config.BulletConfig;
import marowak.dev.dto.config.ShipConfig;

import java.util.Arrays;
import java.util.Map;

public class FactoryConfig {
    private FactoryConfig() {
    }

    public static final BulletConfig kineticCfg = new BulletConfig(
            5,
            6,
            0.1,
            400,
            70,
            10,
            0.05);

    public static final BulletConfig electricCfg = new BulletConfig(
            10,
            3,
            0.05,
            800,
            90,
            100,
            0.5);

    public static final BulletConfig thermalCfg = new BulletConfig(
            10,
            3,
            0.05,
            600,
            90,
            100,
            0.5);

    private static final ShipConfig pilgrimCfg = new ShipConfig(
            Arrays.asList(
                    new Point(1, 16),
                    new Point(110, 16),
                    new Point(127, 63),
                    new Point(109, 111),
                    new Point(1, 110)),
            0.75f
    );

    private static final ShipConfig aggressorCfg = new ShipConfig(
            Arrays.asList(
                    new Point(127, 62),
                    new Point(119, 26),
                    new Point(45, 1),
                    new Point(23, 9),
                    new Point(8, 48),
                    new Point(9, 82),
                    new Point(19, 112),
                    new Point(44, 126),
                    new Point(119, 99)),
            0.75f
    );

    private static final ShipConfig defenderCfg = new ShipConfig(
            Arrays.asList(
                    new Point(1, 16),
                    new Point(110, 16),
                    new Point(127, 63),
                    new Point(109, 111),
                    new Point(1, 110)),
            0.75f
    );

    public static final Map<Integer, ShipConfig> shipsCfg = Map.of(
            1, pilgrimCfg,
            2, aggressorCfg,
            3, defenderCfg
    );
}
