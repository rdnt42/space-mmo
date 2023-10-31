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
                    new Point(10, 10),
                    new Point(85, 10),
                    new Point(115, 30),
                    new Point(125, 60),
                    new Point(115, 90),
                    new Point(85, 115),
                    new Point(10, 115)),
            1f
    );

    private static final ShipConfig aggressorCfg = new ShipConfig(
            Arrays.asList(
                    new Point(10, 45),
                    new Point(25, 10),
                    new Point(40, 5),
                    new Point(110, 30),
                    new Point(120, 60),
                    new Point(110, 90),
                    new Point(40, 120),
                    new Point(25, 115),
                    new Point(10, 90)),
            1f
    );

    private static final ShipConfig defenderCfg = new ShipConfig(
            Arrays.asList(
                    new Point(10, 10),
                    new Point(85, 10),
                    new Point(115, 30),
                    new Point(125, 60),
                    new Point(115, 90),
                    new Point(85, 115),
                    new Point(10, 115)),
            1f
    );

    public static final Map<Integer, ShipConfig> shipsCfg = Map.of(
            1, pilgrimCfg,
            2, aggressorCfg,
            3, defenderCfg
    );
}
