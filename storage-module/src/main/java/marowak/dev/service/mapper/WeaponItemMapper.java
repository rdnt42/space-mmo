package marowak.dev.service.mapper;

import jakarta.inject.Singleton;
import marowak.dev.entity.Item;
import marowak.dev.entity.Weapon;
import marowak.dev.service.item.mark.WeaponItemService;
import message.ItemMessage;
import message.WeaponMessage;

import java.util.function.BiFunction;

@Singleton
public class WeaponItemMapper implements ItemMapper<Weapon>, WeaponItemService {

    @Override
    public ItemMessage map(Item item, Weapon extension) {
        return weaponToMessage.apply(extension, item);
    }

    public static final BiFunction<Weapon, Item, ItemMessage> weaponToMessage =
            (weapon, item) -> ((WeaponMessage.Builder)
                    BuilderHelper.itemToBuilder.apply(item, WeaponMessage.builder()))
                    .damage(weapon.damage())
                    .radius(weapon.radius())
                    .rate(weapon.rate())
                    .damageTypeId(weapon.damageTypeId())
                    .equipmentTypeId(weapon.weaponTypeId())
                    .build();

}
