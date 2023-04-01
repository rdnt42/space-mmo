package marowak.dev.request.message;

public record EngineRequest(
        String characterName,

        int engineTypeId,

        int speed,

        int cost
) {
}
