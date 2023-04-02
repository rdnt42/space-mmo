package marowak.dev.request.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterStateRequest(
        String accountName,
        String characterName,
        boolean isOnline
) implements CharacterRequest {
}
