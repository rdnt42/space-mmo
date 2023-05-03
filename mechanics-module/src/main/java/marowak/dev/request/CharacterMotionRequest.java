package marowak.dev.request;

public record CharacterMotionRequest(
        boolean isUpdate,
        float speed,
        int angle,
        long lastUpdateTime
) {
}
