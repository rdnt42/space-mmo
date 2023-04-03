package marowak.dev.dto.motion;

public record PlayerMotion(
        String playerName,
        int x,
        int y,
        int angle,
        int speed
) {
}
