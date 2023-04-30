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
    private final CharacterCommandService characterCommandService;

    @Topic("characters")
    @SendTo("characters-answer")
    public Publisher<CharacterMessage> receive(Flux<CharacterMessage> messages) {
        return messages
                .doOnError(e -> log.error("Topic \"characters\" receive error: ", e))
                .doOnNext(message -> log.debug("Topic \"characters\" receive message: {}, key: {}", message.getCharacterName(), message.getKey()))
                .flatMap(characterCommandService::executeCommand);
    }
}
