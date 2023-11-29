package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.character.*;
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

    private List<Item> getMockItemsCharOne() {
        List<Item> items = new ArrayList<>();
        Item hull = Hull.builder()
                .id(1)
                .config(0b1001110000001)
                .hp(700)
                .evasion(2)
                .armor(2)
                .build();
        items.add(hull);

        Item engine = Engine.builder()
                .id(2)
                .speed(500)
                .jump(20)
                .build();
        items.add(engine);

        Item weapon1 = Weapon.builder()
                .id(3)
                .slotId(9)
                .damage(10)
                .rate(100)
                .damageTypeId(1)
                .build();
        items.add(weapon1);

        Item weapon2 = Weapon.builder()
                .id(4)
                .slotId(10)
                .damage(10)
                .rate(300)
                .damageTypeId(2)
                .build();
        items.add(weapon2);

        return items;
    }

    private List<Item> getMockItemsCharTwo() {
        List<Item> items = new ArrayList<>();
        Item hull = Hull.builder()
                .id(10)
                .config(8191)
                .hp(300)
                .evasion(2)
                .armor(2)
                .build();
        items.add(hull);

        Item engine = Engine.builder()
                .id(11)
                .speed(500)
                .jump(20)
                .build();
        items.add(engine);

        Item weapon1 = Weapon.builder()
                .id(12)
                .slotId(9)
                .damage(10)
                .rate(100)
                .damageTypeId(3)
                .build();
        items.add(weapon1);

        Item weapon2 = Weapon.builder()
                .id(13)
                .slotId(10)
                .damage(10)
                .rate(300)
                .damageTypeId(2)
                .build();
        items.add(weapon2);

        return items;
    }
}
