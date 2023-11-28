package marowak.dev.service.command.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import marowak.dev.service.item.SpaceItemService;
import reactor.core.CorePublisher;

@RequiredArgsConstructor
@Singleton
public class TakeItemFromSpace implements CharacterRequestCommand<Object, ItemUpdate> {
    private final CharacterItemService characterItemService;
    private final SpaceItemService spaceItemService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CorePublisher<ItemUpdate> execute(Object request, String characterName) {
        ItemUpdate value = objectMapper.convertValue(request, ItemUpdate.class);

        spaceItemService.
        return null;
    }
}
