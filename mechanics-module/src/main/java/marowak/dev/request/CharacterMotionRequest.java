package marowak.dev.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterMotionRequest(
        String accountName,
        String characterName,
        long x,
        long y,
        int angle
) implements CharacterRequest{
}
