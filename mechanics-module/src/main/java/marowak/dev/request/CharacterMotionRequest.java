package marowak.dev.request;

public record CharacterMotionRequest(
        boolean isUpdate,
        int forceTypeId,
        int angle
) {
}
