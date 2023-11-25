package marowak.dev.service.command.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.command.CharacterRequestCommand;
import marowak.dev.service.item.CharacterItemService;
import message.ItemMessage;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class AddCharacterItemCmd implements CharacterRequestCommand<ItemMessage, Void> {
    private final CharacterItemService characterItemService;

    @Override
    public Mono<Void> execute(ItemMessage request, String characterName) {
        return characterItemService.addItem(request);
    }
}
