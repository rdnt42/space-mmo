package marowak.dev.service.item;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.ItemMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(producerClientId = "items-producer")
public class ItemListener {
    private static final String TOPIC_NAME = "items";

    private final ItemCommandService itemCommandService;

    @Topic(TOPIC_NAME)
    @SendTo("items-answer")
    public Publisher<ItemMessage> receive(Flux<ItemMessage> messages) {
        return messages
                .doOnError(e -> log.error("Topic {} receive error: {}", TOPIC_NAME, e))
                .doOnNext(item -> log.info("Topic {} receive key: {}, characterName: {}, item id: {}",
                        TOPIC_NAME, item.getKey(), item.getCharacterName(), item.getId()))
                .flatMap(itemCommandService::executeCommand);
    }
}
