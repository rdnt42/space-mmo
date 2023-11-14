package marowak.dev.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import marowak.dev.service.mapper.ItemMapper;
import marowak.dev.storage.ItemTypeStorage;

import java.util.Set;

@Factory
public class ItemTypeStorageConfig {

    @Bean
    public ItemTypeStorage<ItemMapper<?>> itemMapper(Set<ItemMapper<?>> mappers) {
        return new ItemTypeStorage<>(mappers);
    }
}
