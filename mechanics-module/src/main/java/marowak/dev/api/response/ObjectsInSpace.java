package marowak.dev.api.response;

import marowak.dev.api.response.item.ItemInSpaceView;

import java.util.List;

public record ObjectsInSpace(
        List<CharacterView> characters,
        List<ItemInSpaceView> itemsInSpace,
        List<BulletBodyView> bullets
) {
}
