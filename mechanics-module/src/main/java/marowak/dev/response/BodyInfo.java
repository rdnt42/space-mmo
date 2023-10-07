package marowak.dev.response;

import lombok.Builder;

@Builder
public record BodyInfo(
        String id,
        double x,
        double y,
        int angle,
        float speed
) {
}
