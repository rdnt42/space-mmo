package marowak.dev.config;


import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.character.*;
import marowak.dev.service.item.ItemService;

@Factory
public class CharacterServiceConfig {
    @Bean
    @Requires(property = "mock-state-enabled", value = "false")
    public CharacterService getService(
            CharactersClient charactersClient,
            CharacterShipService characterShipService,
            ObjectInfoService objectInfoService,
            ItemService itemService) {
        return new CharacterServiceImpl(charactersClient, characterShipService, objectInfoService, itemService);
    }

    @Bean
    @Requires(property = "mock-state-enabled", value = "true")
    public CharacterService getMockService(CharacterShipService characterShipService) {
        return new MockCharacterServiceImpl(characterShipService);
    }

}
