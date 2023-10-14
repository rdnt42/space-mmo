package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import org.dyn4j.dynamics.Body;

import java.util.List;

public interface WorldService {
    void updateWorld();

    @Async
    void calculateObjects();

    void createBody(Body body);

    boolean removeBody(Body body);

    <T extends Body> List<Body> getBodies(Class<T> tClass);

    <T extends Body> Body getBody(Class<T> tClass, String id);

}
