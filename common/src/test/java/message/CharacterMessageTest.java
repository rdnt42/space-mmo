package message;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static fixtures.CharacterMessageFixture.aCharacterMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterMessageTest {

    @Test
    void serialize_CharacterMessage_to_bytes() throws IOException {
        JsonMapper jsonMapper = new JsonMapper();
        CharacterMessage expectedMessage = aCharacterMessage().build();

        byte[] bytes = jsonMapper.writeValueAsBytes(expectedMessage);
        CharacterMessage result = jsonMapper.readValue(bytes, CharacterMessage.class);

        assertEquals(expectedMessage.getAccountName(), result.getAccountName());
        assertEquals(expectedMessage.getCharacterName(), result.getCharacterName());
    }

}