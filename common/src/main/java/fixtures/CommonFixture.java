package fixtures;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class CommonFixture {
    private static final Random random = new Random();

    private CommonFixture() {
    }

    public static <T> T randomEnum(Class<T> e) {
        var values = e.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    public static String aString() {
        return RandomStringUtils.randomAlphanumeric(24);
    }

    public static String aString(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }
}
