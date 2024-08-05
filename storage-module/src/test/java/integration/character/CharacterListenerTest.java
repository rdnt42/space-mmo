package integration.character;

import fixtures.CharacterFixture;
import fixtures.Fixture;
import integration.config.IntegrationTest;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import marowak.dev.entity.Character;
import message.CharacterMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static keys.CharacterMessageKey.CHARACTERS_GET_ONE;
import static keys.CharacterMessageKey.CHARACTER_CREATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterListenerTest extends IntegrationTest {

    @KafkaClient
    interface CharacterProducer {
        @Topic("characters")
        void produce(CharacterMessage message);
    }

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    static class CharacterAnswerConsumer {
        CharacterMessage consumed;

        @Topic("characters-answer")
        public void consume(CharacterMessage message) {
            consumed = message;
        }
    }

    @Test
    void create_character(CharacterProducer producer, CharacterAnswerConsumer answer) {
        CharacterMessage message = Fixture.aCharacterMessage()
                .key(CHARACTER_CREATE)
                .build();
        producer.produce(message);

        await().atMost(5, SECONDS).until(() -> answer.consumed != null);
        assertEquals(answer.consumed.getCharacterName(), message.getCharacterName());
        assertEquals(answer.consumed.getAccountName(), message.getAccountName());
    }

    @Test
    void get_character(CharacterProducer producer, CharacterAnswerConsumer answer) {
        Character character = CharacterFixture.aCharacter().build();
        repository.saveEntity(character);

        CharacterMessage message = Fixture.aCharacterMessage()
                .key(CHARACTERS_GET_ONE)
                .characterName(character.characterName())
                .build();
        producer.produce(message);

        await().atMost(5, SECONDS).until(() -> answer.consumed != null);
        assertEquals(answer.consumed.getCharacterName(), character.characterName());
        assertEquals(answer.consumed.getAccountName(), character.accountName());
    }

}