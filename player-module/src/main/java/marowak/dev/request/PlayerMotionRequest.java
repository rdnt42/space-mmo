package marowak.dev.request;

public record PlayerMotionRequest(
        boolean isUpdate,
        int speed,
        int angle
) {
}
