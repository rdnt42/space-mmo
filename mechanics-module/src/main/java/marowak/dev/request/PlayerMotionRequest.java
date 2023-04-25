package marowak.dev.request;

public record PlayerMotionRequest(
        boolean isUpdate,
        float speed,
        int angle
) {
}
