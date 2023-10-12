package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Hull;
import marowak.dev.enums.ItemTypes;
import marowak.dev.response.BodyInfo;
import marowak.dev.response.CharacterInfo;
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

    public Mono<CharacterInfo> getCharacterInfo(String playerName) {
        return characterMotionService.getCharacter(playerName)
                .flatMap(info -> itemService.getFirstEquippedItem(info.id(), ItemTypes.ITEM_TYPE_HULL)
                        .map(Hull.class::cast)
                        .map(hull -> toCharacterResponse.apply(info, hull, info.id())));
    }

    public Flux<CharacterInfo> getCharactersInfo(String playerName) {
        return characterMotionService.getCharactersInRange(playerName)
                .flatMap(info -> itemService.getFirstEquippedItem(info.id(), ItemTypes.ITEM_TYPE_HULL)
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
