package fixtures;

import marowak.dev.entity.Item;
import marowak.dev.entity.Weapon;

import java.util.Random;

import static fixtures.CommonFixture.aString;

public class ItemFixture {
    private static final Random random = new Random();

    private static short nextShort() {
        return nextShort(32767);
    }

    private static short nextShort(int count) {
        return (short) random.nextInt(count);
    }

    public static Item.ItemBuilder anItem() {
        return Item.builder()
                .id(random.nextLong())
                .itemTypeId(random.nextInt(1, 10))
                .x(random.nextDouble())
                .y(random.nextDouble())
                .cost(random.nextInt())
                .dscRu(aString())
                .nameRu(aString())
                .characterName(aString())
                .slotId(random.nextInt())
                .storageId(random.nextInt(4))
                .upgradeLevel((short) random.nextInt(100));
    }

    public static Weapon.WeaponBuilder aWeapon() {
        return Weapon.builder()
                .id(random.nextLong())
                .damage(nextShort())
                .radius(nextShort())
                .rate(nextShort())
                .damage(nextShort())
                .weaponTypeId(random.nextInt(1, 4))
                .damageTypeId(nextShort(5));

    }
}
