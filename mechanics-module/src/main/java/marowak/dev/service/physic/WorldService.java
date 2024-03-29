package marowak.dev.service.physic;

import org.dyn4j.dynamics.Body;

import java.util.List;

public interface WorldService {
    void updateWorld();

    void createBody(Body body);

    void removeBody(Body body);

    void removeBodies(List<Body> bodies);

    <T extends Body> List<T> getBodies(Class<T> tClass);

    <T extends Body> T getBody(Class<T> tClass, String id);

}
