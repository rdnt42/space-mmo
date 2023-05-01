package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.CharacterMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(producerClientId = "characters-producer")
public class CharacterListener {
    private static final String TOPIC_NAME = "characters";
    private final CharacterCommandService characterCommandService;

    @Topic(TOPIC_NAME)
    @SendTo("characters-answer")
    public Publisher<CharacterMessage> receive(Flux<CharacterMessage> messages) {
        return messages
                .doOnError(e -> log.error("Topic {} receive error: {}", TOPIC_NAME, e))
                .doOnNext(message -> log.info("Topic {} receive key: {}, character name: {}",
                        TOPIC_NAME, message.getKey(), message.getCharacterName()))
                .flatMap(characterCommandService::executeCommand);
    }
}
