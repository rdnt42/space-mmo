package marowak.dev.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterRequest(
        String accountName,
        String characterName,
        int x,
        int y,
        int angle
) {
}
