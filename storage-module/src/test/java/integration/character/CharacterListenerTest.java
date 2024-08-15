package integration.character;

import fixtures.CharacterFixture;
import integration.config.IntegrationTest;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import marowak.dev.entity.Character;
import message.CharacterMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static fixtures.CharacterMessageFixture.aCharacterMessage;
import static fixtures.CharacterMessageFixture.aString;
import static java.util.concurrent.TimeUnit.SECONDS;
import static keys.CharacterMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        private boolean wasCaught;
        ConcurrentLinkedDeque<CharacterMessage> messages = new ConcurrentLinkedDeque<>();

        @Topic("characters-answer")
        public void consume(CharacterMessage message) {
            messages.add(message);
            wasCaught = true;
        }

        public boolean caught() {
            if (wasCaught) {
                wasCaught = false;

                return true;
            }

            return false;
        }

        public CharacterMessage getCaught() {
            return messages.getFirst();
        }

        public void clear() {
            messages.clear();
            wasCaught = false;
        }

    }

    @AfterEach
    void clear(CharacterAnswerConsumer answer) {
        answer.clear();
    }

    @Test
    void create_character(CharacterProducer producer, CharacterAnswerConsumer answer) {
        var charName = aString();
        var accountName = aString();
        CharacterMessage message = aCharacterMessage()
                .key(CHARACTER_CREATE)
                .characterName(charName)
                .accountName(accountName)
                .build();
        producer.produce(message);

        await().atMost(5, SECONDS).until(answer::caught);
        var res = answer.getCaught();

        assertEquals(CHARACTER_CREATE, res.getKey());
        assertEquals(charName, res.getCharacterName());
        assertEquals(accountName, res.getAccountName());
    }

    @Test
    void get_character(CharacterProducer producer, CharacterAnswerConsumer answer) {
        var charName = aString();
        var accountName = aString();
        Character character = CharacterFixture.aCharacter()
                .characterName(charName)
                .accountName(accountName)
                .build();
        repository.saveEntity(character);

        CharacterMessage message = aCharacterMessage()
                .key(CHARACTERS_GET_ONE)
                .characterName(charName)
                .accountName(accountName)
                .build();
        producer.produce(message);

        await().atMost(5, SECONDS).until(answer::caught);
        var res = answer.getCaught();

        assertEquals(CHARACTERS_GET_ONE, res.getKey());
        assertEquals(charName, res.getCharacterName());
        assertEquals(accountName, res.getAccountName());
    }

    @Test
    void get_all_online_characters(CharacterProducer producer, CharacterAnswerConsumer answer) {
        var charName1 = aString();
        var charName2 = aString();
        var accountName = aString();
        var charNames = List.of(charName1, charName2);

        Character character1 = CharacterFixture.aCharacter()
                .characterName(charName1)
                .accountName(accountName)
                .online(true)
                .build();
        repository.saveEntity(character1);

        Character character2 = CharacterFixture.aCharacter()
                .characterName(charName2)
                .accountName(accountName)
                .online(true)
                .build();
        repository.saveEntity(character2);

        CharacterMessage message = aCharacterMessage()
                .key(CHARACTERS_GET_ALL)
                .build();
        producer.produce(message);

        await().atMost(5, SECONDS).until(answer::caught);

        var res1 = answer.getCaught();
        var res2 = answer.getCaught();

        assertEquals(CHARACTERS_GET_ALL, res1.getKey());
        assertEquals(accountName, res1.getAccountName());
        assertEquals(CHARACTERS_GET_ALL, res2.getKey());
        assertEquals(accountName, res2.getAccountName());
        assertTrue(charNames.containsAll(List.of(res1.getCharacterName(), res2.getCharacterName())));
    }

}