package marowak.dev.api.request;

public record CharacterMotionRequest(
        boolean isUpdate,
        int forceTypeId,
        int angle
) {
}
