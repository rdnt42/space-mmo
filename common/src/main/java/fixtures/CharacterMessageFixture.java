package fixtures;

import keys.CharacterMessageKey;
import message.CharacterMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static fixtures.CommonFixture.randomEnum;

public class CharacterMessageFixture {
    private CharacterMessageFixture() {
    }

    private static final Random random = new Random();

    public static CharacterMessage.Builder aCharacterMessage() {
        return CharacterMessage.builder()
                .key(randomEnum(CharacterMessageKey.class))
                .characterName(RandomStringUtils.randomAlphanumeric(10))
                .accountName(RandomStringUtils.randomAlphanumeric(10))
                .experience(random.nextInt())
                .x(random.nextDouble())
                .y(random.nextDouble())
                .angle(random.nextInt())
                .online(random.nextBoolean());
    }

}
