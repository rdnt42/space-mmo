package marowak.dev.request;

public record CharacterShootingRequest(
        boolean isShooting,
        float angle,
        long lastUpdateTime

) {
}
