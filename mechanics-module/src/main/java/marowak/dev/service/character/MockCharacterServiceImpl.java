package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.character.CharacterShip;
import marowak.dev.dto.item.EngineDto;
import marowak.dev.dto.item.HullDto;
import marowak.dev.dto.item.ItemDto;
import marowak.dev.dto.item.WeaponDto;
import marowak.dev.dto.motion.CharacterMotion;
import message.CharacterMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Singleton
public class MockCharacterServiceImpl implements CharacterService {

    private final CharacterShipService characterShipService;

    @Override
    public Mono<Void> sendCharactersUpdate() {
        return Mono.empty();
    }

    @Override
    public Mono<Void> initCharacter(CharacterMessage message) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> leavingPlayer(String characterName) {
        return characterShipService.removeCharacter(characterName)
                .then();
    }

    @Override
    public Mono<Void> sendCharacterState(String characterName, boolean isOnline) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> sendInitCharacter(CharacterMessageKey key, String characterName) {
        CharacterMotion char1 = CharacterMotion.builder()
                .characterName("p1")
                .x(-150)
                .y(-150)
                .speed(0)
                .angle(0)
                .build();

        Flux<CharacterShip> fluxOne = characterShipService.addCharacter(char1)
                .flatMapMany(character -> Flux.fromIterable(getMockItemsCharOne())
                        .flatMap(item -> characterShipService.addItem("p1", item))
                        .collectList()
                        .map(items -> character));

        CharacterMotion char2 = CharacterMotion.builder()
                .characterName("sd")
                .x(150)
                .y(150)
                .speed(0)
                .angle(0)
                .build();
        Flux<CharacterShip> fluxTwo = characterShipService.addCharacter(char2)
                .flatMapMany(character -> Flux.fromIterable(getMockItemsCharTwo())
                        .flatMap(item -> characterShipService.addItem("sd", item))
                        .collectList()
                        .map(items -> character));


        return Flux.merge(fluxOne, fluxTwo)
                .then();
    }

    @Override
    public Mono<Void> updateCharacterMotion(CharacterMotionRequest request, String characterName) {
        if (!request.isUpdate()) {
            return Mono.empty();
        }

        return characterShipService.updateShipMotion(request, characterName);
    }

    private List<ItemDto> getMockItemsCharOne() {
        var dsc = "Описание";
        List<ItemDto> items = new ArrayList<>();
        ItemDto hull = HullDto.builder()
                .id(1)
                .slotId(8)
                .storageId(1)
                .typeId(8)
                .upgradeLevel(0)
                .cost(10000)
                .name("Корпус рейнджера")
                .dsc(dsc)
                .config(0b1001110000001)
                .hp(700)
                .evasion(2)
                .armor(2)
                .equipmentTypeId(2)
                .build();
        items.add(hull);

        ItemDto engine = EngineDto.builder()
                .id(2)
                .slotId(1)
                .storageId(1)
                .typeId(1)
                .upgradeLevel(0)
                .cost(10000)
                .name("Двигатель")
                .dsc(dsc)
                .speed(500)
                .jump(20)
                .equipmentTypeId(1)
                .build();
        items.add(engine);

        ItemDto weapon1 = WeaponDto.builder()
                .id(3)
                .slotId(9)
                .storageId(1)
                .typeId(9)
                .upgradeLevel(0)
                .cost(10000)
                .name("Оружие 1")
                .dsc(dsc)
                .damage(10)
                .rate(100)
                .damageTypeId(1)
                .equipmentTypeId(1)
                .build();
        items.add(weapon1);

        ItemDto weapon2 = WeaponDto.builder()
                .id(4)
                .slotId(10)
                .storageId(1)
                .typeId(9)
                .upgradeLevel(0)
                .cost(10000)
                .name("Оружие 2")
                .dsc(dsc)
                .damage(10)
                .rate(300)
                .damageTypeId(2)
                .equipmentTypeId(2)
                .build();
        items.add(weapon2);

        return items;
    }

    private List<ItemDto> getMockItemsCharTwo() {
        var dsc = "Описание 2";
        List<ItemDto> items = new ArrayList<>();
        ItemDto hull = HullDto.builder()
                .id(10)
                .slotId(8)
                .storageId(1)
                .typeId(8)
                .upgradeLevel(0)
                .cost(10000)
                .name("Корпус рейнджера")
                .dsc(dsc)
                .config(8191)
                .hp(300)
                .evasion(2)
                .armor(2)
                .equipmentTypeId(1)
                .build();
        items.add(hull);

        ItemDto engine = EngineDto.builder()
                .id(11)
                .slotId(1)
                .storageId(1)
                .typeId(1)
                .upgradeLevel(0)
                .cost(10000)
                .name("Двигатель")
                .dsc(dsc)
                .speed(500)
                .jump(20)
                .equipmentTypeId(1)
                .build();
        items.add(engine);

        ItemDto weapon1 = WeaponDto.builder()
                .id(12)
                .slotId(9)
                .storageId(1)
                .typeId(9)
                .upgradeLevel(0)
                .cost(10000)
                .name("Оружие 1")
                .dsc(dsc)
                .damage(10)
                .rate(100)
                .damageTypeId(3)
                .equipmentTypeId(1)
                .build();
        items.add(weapon1);

        ItemDto weapon2 = WeaponDto.builder()
                .id(13)
                .slotId(10)
                .storageId(1)
                .typeId(9)
                .upgradeLevel(0)
                .cost(10000)
                .name("Оружие 2")
                .dsc(dsc)
                .damage(10)
                .rate(300)
                .damageTypeId(2)
                .equipmentTypeId(2)
                .build();
        items.add(weapon2);

        return items;
    }
}
