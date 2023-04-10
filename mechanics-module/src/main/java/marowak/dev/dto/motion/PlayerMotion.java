package marowak.dev.dto.motion;

public record PlayerMotion(
        String playerName,
        long x,
        long y,
        int angle,
        int speed
) {
}
