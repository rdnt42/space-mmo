package marowak.dev.api.request;

public record CharacterShootingRequest(
        boolean isShooting,
        int angle,
        long lastUpdateTime

) {
}
