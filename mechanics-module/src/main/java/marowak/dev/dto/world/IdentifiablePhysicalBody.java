package marowak.dev.dto.world;

import lombok.Getter;
import org.dyn4j.dynamics.Body;

@Getter
public abstract class IdentifiablePhysicalBody extends Body {

    private final String id;

    protected IdentifiablePhysicalBody(String id) {
        this.id = id;
    }
}
