package marowak.dev.request.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterMotionRequest(
        String accountName,
        String characterName,
        double x,
        double y,
        int angle
) implements CharacterRequest {
}
