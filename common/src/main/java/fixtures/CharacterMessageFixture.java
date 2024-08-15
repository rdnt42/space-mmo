package fixtures;

import keys.CharacterMessageKey;
import message.CharacterMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class CharacterMessageFixture {
    private CharacterMessageFixture() {
    }

    private static final Random random = new Random();

    private static <T> T randomEnum(Class<T> e) {
        var values = e.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

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

    public static String aString() {
        return RandomStringUtils.randomAlphanumeric(24);
    }

}
