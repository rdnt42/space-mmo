package marowak.dev.service.item;

import message.*;

public class ItemDescriptorHelper {

    public static String getDsc(ItemMessage item) {
        if (item.getDsc() != null) return null;

        return switch (item) {
            case EngineMessage e -> getItemDsc(e);
            case FuelTankMessage f -> getItemDsc(f);
            case CargoHookMessage c -> getItemDsc(c);
            case HullMessage h -> getItemDsc(h);
            case WeaponMessage w -> getItemDsc(w);
            default -> "";
        };
    }

    private static String getItemDsc(EngineMessage engine) {
        return String.format("Позволяет развивать скорость до %s \n единиц и совершать гиперпрыжок \n на %s парсек",
                engine.getSpeed(), engine.getJump());
    }

    private static String getItemDsc(FuelTankMessage fuelTank) {
        return String.format("Емкость бака %s", fuelTank.getCapacity());
    }

    private static String getItemDsc(CargoHookMessage cargoHook) {
        return String.format("Мощность захвата %s", cargoHook.getLoadCapacity());
    }

    private static String getItemDsc(HullMessage hull) {
        return String.format("""
                        Стойкость корпуса: %s единиц,\s
                        обшивка блокирует до %s единиц ущерба,\s
                        вероятность уворота: %s""",
                hull.getHp(), hull.getArmor(), hull.getEvasion());
    }

    private static String getItemDsc(WeaponMessage weapon) {
        var type = switch (weapon.getDamageTypeId()) {
            case 1 -> "кинетического";
            case 2 -> "электромагнитного'";
            case 3 -> "термического";
            case 4 -> "раектного";
            default -> "";
        };

        return String.format("Наносит %s %s урона \n в радиусе %s", weapon.getDamage(), type, weapon.getRadius());
    }
}
