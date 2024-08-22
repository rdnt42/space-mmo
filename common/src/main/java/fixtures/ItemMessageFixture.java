package fixtures;

import keys.ItemMessageKey;
import message.ItemMessage;
import message.WeaponMessage;

import java.util.Random;

import static fixtures.CommonFixture.aString;
import static fixtures.CommonFixture.randomEnum;

public class ItemMessageFixture {
    private ItemMessageFixture() {
    }

    private static final Random random = new Random();

    private static ItemMessage.Builder fillItem(ItemMessage.Builder builder) {
        return builder
                .key(randomEnum(ItemMessageKey.class))
                .id(random.nextLong())
                .slotId(random.nextInt())
                .storageId(random.nextInt())
                .characterName(aString())
                .typeId(random.nextInt())
                .upgradeLevel(random.nextInt())
                .cost(random.nextInt())
                .name(aString())
                .name(aString())
                .dsc(aString())
                .x(random.nextDouble())
                .y(random.nextDouble());
    }

    public static WeaponMessage.Builder aWeaponMessage() {
        var builder = (WeaponMessage.Builder) fillItem(WeaponMessage.builder());
        return builder
                .damage(random.nextInt())
                .radius(random.nextInt())
                .rate(random.nextInt())
                .damageTypeId(random.nextInt())
                .equipmentTypeId(random.nextInt());
    }

}
