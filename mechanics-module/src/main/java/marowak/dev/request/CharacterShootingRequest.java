package marowak.dev.request;

public record CharacterShootingRequest(
        boolean isShooting,
        int angle,
        long lastUpdateTime

) {
}
