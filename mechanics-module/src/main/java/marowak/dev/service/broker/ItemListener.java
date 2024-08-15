package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.item.ItemCommandService;
import message.ItemMessage;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class ItemListener {
    private static final String TOPIC_NAME = "items-answer";

    private final ItemCommandService itemCommandService;

    @Topic(TOPIC_NAME)
    public void receive(Flux<ItemMessage> items) {
        items
                .doOnError(e -> log.error("Topic {} receive error", TOPIC_NAME, e))
                .doOnNext(message -> log.debug("Topic {} receive message: {}, key: {}", TOPIC_NAME, message.getCharacterName(), message.getKey()))
                .flatMap(itemCommandService::executeCommand)
                .subscribe();
    }
}
