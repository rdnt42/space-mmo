package marowak.dev.request;

public record CharacterMotionRequest(
        String characterName,
        int x,
        int y,
        int angle
) {
}
