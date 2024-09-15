package integration.item;

import integration.setup.IntegrationTest;
import integration.setup.KafkaReadWriter;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import marowak.dev.entity.Character;
import marowak.dev.entity.Item;
import marowak.dev.entity.Weapon;
import message.ItemMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static fixtures.CharacterFixture.aCharacter;
import static fixtures.CommonFixture.aString;
import static fixtures.ItemFixture.aWeapon;
import static fixtures.ItemFixture.anItem;
import static fixtures.ItemMessageFixture.aWeaponMessage;
import static keys.ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemListenerTest extends IntegrationTest {
    private final KafkaReadWriter<ItemMessage, ItemMessage> kafka = new KafkaReadWriter<>(
            IntegrationTest.properties,
            "items",
            "items-answer",
            ItemMessage.class
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
    void get_item() {
        var charName = aString();
        var nameRu = aString();

        createAndSaveItemCharacter(charName, nameRu);
        ItemMessage message = aWeaponMessage()
                .key(ITEMS_GET_FOR_ONE_CHARACTER)
                .characterName(charName)
                .name(nameRu)
                .build();

        var res = kafka.produceAndReceive(message);

        assertEquals(ITEMS_GET_FOR_ONE_CHARACTER, res.getKey());
        assertEquals(charName, res.getCharacterName());
        assertEquals(nameRu, res.getName());
    }

    private void createAndSaveItemCharacter(String charName, String name) {
        Character character = aCharacter()
                .characterName(charName)
                .accountName(aString())
                .online(true)
                .build();

        Item item = anItem()
                .itemTypeId(9)
                .nameRu(name)
                .characterName(charName)
                .build();
        Weapon weapon = aWeapon()
                .id(item.id())
                .weaponTypeId(1)
                .damageTypeId((short) 1)
                .build();

        repository.saveEntity(character);
        repository.saveEntity(item);
        repository.saveEntity(weapon);
    }
}
