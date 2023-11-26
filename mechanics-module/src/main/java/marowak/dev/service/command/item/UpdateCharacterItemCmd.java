package marowak.dev.service.command.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class UpdateCharacterItemCmd implements CharacterRequestCommand<Object, ItemUpdate> {
    private final CharacterItemService characterItemService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Mono<ItemUpdate> execute(Object request, String characterName) {
        ItemUpdate value = objectMapper.convertValue(request, ItemUpdate.class);

        return characterItemService.updateItem(value, characterName);
    }
}
