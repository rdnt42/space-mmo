package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.item.ItemStorage;

@RequiredArgsConstructor
@Singleton
public class ItemInSpaceInit {
    private final ItemStorage itemStorage;

    @EventListener
    public void initData(StartupEvent startupEvent) {
        itemStorage.sendGetItem(ItemMessageKey.ITEMS_GET_IN_SPACE, null)
                .subscribe();
    }
}
