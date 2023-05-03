package marowak.dev.request;

public record CharacterInventoryItemRequest(
        long itemId,
        Integer slotId,
        boolean equipped
) {
}
