package fixtures;

import marowak.dev.entity.Character;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class CharacterFixture {
    private static final Random random = new Random();

    public static Character.CharacterBuilder aCharacter() {
        return Character.builder()
                .characterName(RandomStringUtils.randomAlphanumeric(10))
                .accountName(RandomStringUtils.randomAlphanumeric(10))
                .experience(random.nextInt())
                .x(random.nextDouble())
                .y(random.nextDouble())
                .angle((short) random.nextInt(360))
                .online(random.nextBoolean());
    }
}
