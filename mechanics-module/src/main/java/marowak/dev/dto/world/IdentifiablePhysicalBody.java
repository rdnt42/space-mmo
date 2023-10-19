package marowak.dev.dto.world;

import lombok.Getter;
import org.dyn4j.dynamics.Body;

@Getter
public abstract class IdentifiablePhysicalBody extends Body {

    private final String id;
    private final String creatorId;

    protected IdentifiablePhysicalBody(String id, String creatorId) {
        this.id = id;
        this.creatorId = creatorId;
    }
}
