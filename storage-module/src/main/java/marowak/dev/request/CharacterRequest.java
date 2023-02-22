package marowak.dev.request;

public record CharacterRequest(
        String accountName,
        String characterName,
        int x,
        int y,
        int angle
) {
}
