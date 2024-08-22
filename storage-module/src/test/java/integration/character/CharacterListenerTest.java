package integration.character;

import integration.setup.IntegrationTest;
import integration.setup.KafkaReadWriter;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import marowak.dev.entity.Character;
import message.CharacterMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static fixtures.CharacterFixture.aCharacter;
import static fixtures.CharacterMessageFixture.aCharacterMessage;
import static fixtures.CommonFixture.aString;
import static keys.CharacterMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterListenerTest extends IntegrationTest {
    private final KafkaReadWriter<CharacterMessage, CharacterMessage> kafka = new KafkaReadWriter<>(
            IntegrationTest.properties,
            "characters",
            "characters-answer",
            CharacterMessage.class
    );

    @BeforeEach
    void setup() {
        kafka.clear();
    }

    @AfterAll
    void close() {
        kafka.close();
    }

    @Test
    void create_character() {
        var charName = aString();
        var accountName = aString();
        CharacterMessage message = aCharacterMessage()
                .key(CHARACTER_CREATE)
                .characterName(charName)
                .accountName(accountName)
                .build();

        var res = kafka.produceAndReceive(message);

        assertEquals(CHARACTER_CREATE, res.getKey());
        assertEquals(charName, res.getCharacterName());
        assertEquals(accountName, res.getAccountName());
    }

    @Test
    void get_character() {
        var charName = aString();
        var accountName = aString();
        createAndSaveCharacter(charName, accountName);

        CharacterMessage message = aCharacterMessage()
                .key(CHARACTERS_GET_ONE)
                .characterName(charName)
                .accountName(accountName)
                .build();

        var res = kafka.produceAndReceive(message);

        assertEquals(CHARACTERS_GET_ONE, res.getKey());
        assertEquals(charName, res.getCharacterName());
        assertEquals(accountName, res.getAccountName());
    }

    @Test
    void get_all_online_characters() {
        var charName1 = aString();
        var charName2 = aString();
        var accountName = aString();
        var charNames = List.of(charName1, charName2);

        createAndSaveCharacter(charName1, accountName);
        createAndSaveCharacter(charName2, accountName);
        CharacterMessage message = aCharacterMessage()
                .key(CHARACTERS_GET_ALL)
                .build();

        kafka.produce(message);
        var res1 = kafka.receive();
        var res2 = kafka.receive();

        assertEquals(CHARACTERS_GET_ALL, res1.getKey());
        assertEquals(accountName, res1.getAccountName());
        assertEquals(CHARACTERS_GET_ALL, res2.getKey());
        assertEquals(accountName, res2.getAccountName());
        assertTrue(charNames.containsAll(List.of(res1.getCharacterName(), res2.getCharacterName())));
    }

    private void createAndSaveCharacter(String charName, String accountName) {
        Character character = aCharacter()
                .characterName(charName)
                .accountName(accountName)
                .online(true)
                .build();
        repository.saveEntity(character);
    }

}