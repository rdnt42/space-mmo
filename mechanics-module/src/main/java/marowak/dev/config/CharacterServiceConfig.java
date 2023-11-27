package marowak.dev.config;


import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import marowak.dev.service.ObjectInfoService;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.character.CharacterServiceImpl;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.character.MockCharacterServiceImpl;
import marowak.dev.service.item.CharacterItemService;

@Factory
public class CharacterServiceConfig {
    @Bean
    @Requires(property = "mock-state-enabled", value = "false")
    public CharacterService getService(
            CharactersClient charactersClient,
            CharacterShipService characterShipService,
            ObjectInfoService objectInfoService,
            CharacterItemService characterItemService) {
        return new CharacterServiceImpl(charactersClient, characterShipService, objectInfoService, characterItemService);
    }

    @Bean
    @Requires(property = "mock-state-enabled", value = "true")
    public CharacterService getMockService(CharacterShipService characterShipService) {
        return new MockCharacterServiceImpl(characterShipService);
    }

}
