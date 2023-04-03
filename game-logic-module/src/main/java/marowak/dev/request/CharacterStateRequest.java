package marowak.dev.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CharacterStateRequest(
        String accountName,
        String characterName,
        boolean isOnline
) implements CharacterRequest{
}
