package marowak.dev.request;

public record PlayerMotionRequest(
        boolean isUpdate,
        Integer speed,
        Integer angle
) {
}
