package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.item.ItemService;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharacterService characterService;
    private final ItemService itemService;

    @EventListener
    public void initData(StartupEvent startupEvent) {
        characterService.sendInitCharacter(CharacterMessageKey.CHARACTERS_GET_ALL, "");
        itemService.sendGetItems(ItemMessageKey.ITEMS_GET_ALL, "")
                .subscribe();
    }
}
