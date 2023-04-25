package marowak.dev.dto.motion;

public record PlayerMotion(
        String playerName,
        double x,
        double y,
        int angle,
        float speed
) {
}
