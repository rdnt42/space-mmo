package marowak.dev.request;

import marowak.dev.dto.motion.Motion;

public record PlayerMotionRequest(
        Motion motion,
        boolean isUpdate
) {
}
