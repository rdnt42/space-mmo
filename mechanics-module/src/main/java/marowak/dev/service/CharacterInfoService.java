package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInfo;
import marowak.dev.dto.CharactersInfo;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.enums.ItemTypes;
import marowak.dev.service.item.ItemService;
import marowak.dev.service.motion.CharacterMotionService;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInfoService {
    private final CharacterMotionService characterMotionService;
    private final ItemService itemService;

    public Mono<CharacterInfo> getCharacterInfo(String playerName) {
        return characterMotionService.getCharacter(playerName)
                .flatMap(motion -> itemService.getItem(motion.characterName(), ItemTypes.ITEM_TYPE_HULL)
                        .map(item -> toCharacterResponse.apply(motion, (Hull) item, motion.characterName())));
    }

    public Mono<CharactersInfo> getCharactersInfo(String playerName) {
        List<CharacterInfo> infos = characterMotionService.getCharactersInRange(playerName)
                .motions().stream()
                .map(motion -> {
                    var item = itemService.getItemCommon(motion.characterName(), ItemTypes.ITEM_TYPE_HULL);

                    return toCharacterResponse.apply(motion, (Hull) item, motion.characterName());
                })
                .toList();

        return Mono.just(new CharactersInfo(infos));
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
