package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.enums.ItemTypes;
import marowak.dev.response.character.CharacterStateResponse;
import marowak.dev.response.character.CharactersStateResponse;
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

    public Mono<CharacterStateResponse> getCharacterInfo(String playerName) {
        CharacterMotion motion = characterMotionService.getCharacterMotion(playerName);
        Item item = itemService.getItemCommon(motion.characterName(), ItemTypes.ITEM_TYPE_HULL);

        return Mono.just(toCharacterResponse.apply(motion, (Hull) item, motion.characterName()));
    }

    public CharactersStateResponse getCharactersInfo(String playerName) {
        List<CharacterStateResponse> infos = characterMotionService.getCharactersInRange(playerName)
                .motions().stream()
                .map(motion -> {
                    var item = itemService.getItemCommon(motion.characterName(), ItemTypes.ITEM_TYPE_HULL);

                    return toCharacterResponse.apply(motion, (Hull) item, motion.characterName());
                })
                .toList();

        return new CharactersStateResponse(infos);
    }

    private final TriFunction<CharacterMotion, Hull, String, CharacterStateResponse> toCharacterResponse =
            (motion, item, characterName) -> CharacterStateResponse.builder()
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
