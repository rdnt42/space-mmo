package marowak.dev.request;

public record CharacterMotionRequest(
        boolean isUpdate,
        float speed,

        int forceTypeId,
        int angle,
        long lastUpdateTime
) {
}
