package marowak.dev.init;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.CharactersGetMessageKey;
import marowak.dev.enums.EquipmentMessageKey;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.equipment.EquipmentService;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInit {
    private final CharacterService characterService;
    private final EquipmentService equipmentService;

    @EventListener
    public void initCharacters(StartupEvent startupEvent) {
        characterService.sendInitCharacter(CharactersGetMessageKey.CHARACTERS_GET_ALL, "");
        equipmentService.sendGetEquipments(EquipmentMessageKey.EQUIPMENTS_GET_ALL, "");
    }
}
