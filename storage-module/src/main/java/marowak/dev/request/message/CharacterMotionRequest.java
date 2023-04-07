package marowak.dev.request.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterMotionRequest(
        String accountName,
        String characterName,
        int x,
        int y,
        int angle
) implements CharacterRequest {
}