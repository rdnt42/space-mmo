package marowak.dev.api.response;

import marowak.dev.api.response.item.ItemInSpace;

import java.util.List;

public record ObjectsInSpace(
        List<CharacterView> characters,
        List<ItemInSpace> itemsInSpace
) {
}
