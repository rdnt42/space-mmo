package marowak.dev.dto.motion;

public record PlayerMotionRequest(
        Motion motion,
        boolean isUpdate
) {
}
