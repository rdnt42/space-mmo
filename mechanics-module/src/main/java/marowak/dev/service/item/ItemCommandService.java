package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.command.item.AddCharacterItemCmd;
import marowak.dev.service.command.item.AddSpaceItemCmd;
import message.ItemMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class ItemCommandService {
    private final AddCharacterItemCmd addCharacterItemCmd;
    private final AddSpaceItemCmd addSpaceItemCmd;

    public Publisher<Void> executeCommand(ItemMessage message) {
        return switch (message.getKey()) {
            case ITEMS_GET_FOR_ALL_CHARACTERS, ITEMS_GET_FOR_ONE_CHARACTER ->
                    addCharacterItemCmd.execute(message, null);
            case ITEMS_GET_IN_SPACE -> addSpaceItemCmd.execute(message, null);
            default -> Mono.empty();
        };
    }
}
