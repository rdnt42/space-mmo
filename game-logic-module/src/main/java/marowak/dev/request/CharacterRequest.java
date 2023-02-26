package marowak.dev.request;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

@Builder
@Serdeable
public record CharacterRequest(
        String accountName,
        String characterName,
        int x,
        int y,
        int angle
) {
}
