package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import org.dyn4j.dynamics.Body;

public interface WorldService {
    void updateWorld();

    @Async
    void calculateObjects();

    void createBody(Body body);

    boolean removeBody(Body body);

}
