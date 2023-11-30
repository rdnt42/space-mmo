package marowak.dev.service.item;

import marowak.dev.character.*;
import marowak.dev.dto.item.*;
import message.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BuilderHelper {

    private BuilderHelper() {
    }

    public static final BiFunction<ItemMessage, ItemDto.ItemDtoBuilder<?, ?>, ItemDto.ItemDtoBuilder<?, ?>> messageToItemBuilder =
            (message, baseBuilder) -> baseBuilder
                    .id(message.getId())
                    .characterName(message.getCharacterName())
                    .x(message.getX())
                    .y(message.getY())
                    .slotId(message.getSlotId())
                    .storageId(message.getStorageId())
                    .typeId(message.getTypeId())
                    .upgradeLevel(message.getUpgradeLevel())
                    .cost(message.getCost())
                    .name(message.getName())
                    .dsc(ItemDescriptorHelper.getDsc(message));

    public static final Function<EngineMessage, ItemDto> engineMessageToItem = message ->
            ((EngineDto.EngineDtoBuilder<?, ?>) messageToItemBuilder.apply(message, EngineDto.builder()))
                    .speed(message.getSpeed())
                    .jump(message.getJump())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<FuelTankMessage, ItemDto> fuelTankMessageToItem = message ->
            ((FuelTankDto.FuelTankDtoBuilder<?, ?>) messageToItemBuilder.apply(message, FuelTankDto.builder()))
                    .capacity(message.getCapacity())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<CargoHookMessage, ItemDto> cargoHookMessageToItem = message ->
            ((CargoHookDto.CargoHookDtoBuilder<?, ?>) messageToItemBuilder.apply(message, CargoHookDto.builder()))
                    .loadCapacity(message.getLoadCapacity())
                    .radius(message.getRadius())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<HullMessage, ItemDto> hullMessageToItem = message ->
            ((HullDto.HullDtoBuilder<?, ?>) messageToItemBuilder.apply(message, HullDto.builder()))
                    .hp(message.getHp())
                    .evasion(message.getEvasion())
                    .armor(message.getArmor())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .config(message.getConfig())
                    .build();

    public static final Function<WeaponMessage, ItemDto> weaponMessageToItem = message ->
            ((WeaponDto.WeaponDtoBuilder<?, ?>) messageToItemBuilder.apply(message, WeaponDto.builder()))
                    .damage(message.getDamage())
                    .radius(message.getRadius())
                    .rate(message.getRate())
                    .damageTypeId(message.getDamageTypeId())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();


    public static final Function<CargoHookDto, CargoHook> dtoToCargoHook = dto ->
            CargoHook.builder()
                    .id(dto.getId())
                    .loadCapacity(dto.getLoadCapacity())
                    .radius(dto.getRadius())
                    .build();

    public static final Function<ItemDto, CargoItem> dtoToCargoItem = dto ->
            CargoItem.builder()
                    .id(dto.getId())
                    .slotId(dto.getSlotId())
                    .build();

    public static final Function<EngineDto, Engine> dtoToEngine = dto ->
            Engine.builder()
                    .id(dto.getId())
                    .speed(dto.getSpeed())
                    .jump(dto.getJump())
                    .build();

    public static final Function<FuelTankDto, FuelTank> dtoToFuelTank = dto ->
            FuelTank.builder()
                    .id(dto.getId())
                    .capacity(dto.getCapacity())
                    .build();

    public static final Function<HullDto, Hull> dtoToHull = dto ->
            Hull.builder()
                    .id(dto.getId())
                    .hullType(dto.getEquipmentTypeId())
                    .hp(dto.getHp())
                    .evasion(dto.getEvasion())
                    .armor(dto.getArmor())
                    .config(dto.getConfig())
                    .build();

    public static final Function<WeaponDto, Weapon> dtoToWeapon = dto ->
            Weapon.builder()
                    .id(dto.getId())
                    .damage(dto.getDamage())
                    .damageTypeId(dto.getDamageTypeId())
                    .rate(dto.getRate())
                    .radius(dto.getRadius())
                    .slotId(dto.getSlotId())
                    .build();


}
