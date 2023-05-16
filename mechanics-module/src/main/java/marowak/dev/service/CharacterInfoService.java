package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInfo;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.enums.ItemTypes;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.motion.CharacterMotionService;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInfoService {
    private final CharacterMotionService characterMotionService;
    private final ItemService itemService;

    public Flux<CharacterInfo> getCharactersInfo(String playerName) {
        return characterMotionService.getCharactersInRange(playerName)
                .flatMap(motion -> itemService.getItem(motion.characterName(), ItemTypes.ITEM_TYPE_HULL)
                        .map(item -> toCharacterResponse.apply(motion, (Hull) item, playerName)));
    }

    private final TriFunction<CharacterMotion, Hull, String, CharacterInfo> toCharacterResponse =
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
