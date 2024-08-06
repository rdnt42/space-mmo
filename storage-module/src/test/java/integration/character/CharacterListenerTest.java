package integration.character;

import fixtures.Fixture;
import integration.IntegrationTest;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import message.CharacterMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.util.concurrent.TimeUnit.SECONDS;
import static keys.CharacterMessageKey.CHARACTER_CREATE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterListenerTest extends IntegrationTest {
    @KafkaClient
    interface CharacterProducer {
        @Topic("characters")
        void produce(CharacterMessage message);
    }

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    static class CharacterConsumer {
        CharacterMessage consumed;

        @Topic("characters")
        public void consume(CharacterMessage message) {
            consumed = message;
        }
    }

    @Test
    void create_character(CharacterProducer producer, CharacterConsumer consumer) {
        CharacterMessage message = Fixture.aCharacterMessage()
                .key(CHARACTER_CREATE)
                .build();
        producer.produce(message);
        await().atMost(5, SECONDS).until(() -> consumer.consumed != null);

        assertTrue(isEntityExists("characters", "character_name", message.getCharacterName()));
    }


}