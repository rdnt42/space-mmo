package integration.character;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import message.CharacterMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static keys.CharacterMessageKey.CHARACTER_CREATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterListenerTest {
    @Test
    void get_CharacterMessage(CharacterProducer producer, CharacterConsumer consumer) {
        CharacterMessage message = CharacterMessage.builder()
                .key(CHARACTER_CREATE)
                .x(1.1)
                .y(2.2)
                .angle(180)
                .experience(0)
                .online(true)
                .accountName("account")
                .characterName("character")
                .build();
        producer.produce(message);
        await().atMost(5, SECONDS).until(() -> consumer.consumed != null);

        assertEquals(message.getAccountName(), consumer.consumed.getAccountName());
        assertEquals(message.getCharacterName(), consumer.consumed.getCharacterName());
    }

    @KafkaClient
    interface CharacterProducer {
        @Topic("character-test-topic")
        void produce(CharacterMessage message);
    }

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    static class CharacterConsumer {
        CharacterMessage consumed;

        @Topic("character-test-topic")
        public void consume(CharacterMessage message) {
            consumed = message;
        }
    }
}