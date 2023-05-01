package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.character.CharacterCommandService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class CharactersListener {
    private static final String TOPIC_NAME = "characters-answer";
    private final CharacterCommandService characterCommandService;

    @Topic(TOPIC_NAME)
    public void receive(Flux<CharacterMessage> messages) {
        messages
                .doOnError(e -> log.error("Topic {} receive error: ", TOPIC_NAME, e))
                .doOnNext(message -> log.debug("Topic {} receive message: {}, key: {}", TOPIC_NAME, message.getCharacterName(), message.getKey()))
                .flatMap(characterCommandService::executeCommand)
                .subscribe();
    }
}
