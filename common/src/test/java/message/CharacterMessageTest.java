package message;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static keys.CharacterMessageKey.CHARACTER_CREATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterMessageTest {

    @Test
    void serialize_CharacterMessage_to_bytes() throws IOException {
        JsonMapper jsonMapper = new JsonMapper();
        CharacterMessage expectedMessage = CharacterMessage.builder()
                .key(CHARACTER_CREATE)
                .x(1.1)
                .y(2.2)
                .angle(180)
                .experience(0)
                .online(true)
                .accountName("account")
                .characterName("character")
                .build();

        byte[] bytes = jsonMapper.writeValueAsBytes(expectedMessage);
        CharacterMessage result = jsonMapper.readValue(bytes, CharacterMessage.class);

        assertEquals(expectedMessage.getAccountName(), result.getAccountName());
        assertEquals(expectedMessage.getCharacterName(), result.getCharacterName());
    }

}