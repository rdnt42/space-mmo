package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Hull;
import marowak.dev.enums.ItemType;
import marowak.dev.response.BodyInfo;
import marowak.dev.response.CharacterInfo;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.motion.CharacterMotionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInfoService {
    private final CharacterMotionService characterMotionService;
    private final ItemService itemService;
    private final CharacterShipService characterShipService;

    public Mono<CharacterInfo> getCharacterInfo(String playerName) {
        return characterShipService.getCharacter(playerName);
    }

    public Flux<CharacterInfo> getCharactersInfo(String playerName) {
        return characterMotionService.getCharactersInRange(playerName)
                .flatMap(info -> itemService.getFirstEquippedItem(info.id(), ItemType.ITEM_TYPE_HULL)
                        .map(item -> toCharacterResponse.apply(info, (Hull) item, info.id())));
    }

    private final TriFunction<BodyInfo, Hull, String, CharacterInfo> toCharacterResponse =
            (motion, item, characterName) -> CharacterInfo.builder()
                    .characterName(characterName)
                    .x(motion.x())
                    .y(motion.y())
                    .angle(motion.angle())
                    .speed(motion.speed())
                    .shipTypeId(item == null ? null : item.getEquipmentTypeId())
                    .hp(item == null ? null : item.getHp())
                    .evasion(item == null ? null : item.getEvasion())
                    .armor(item == null ? null : item.getArmor())
                    .build();
}
