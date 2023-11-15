package marowak.dev.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import marowak.dev.service.mapper.ItemMapper;
import marowak.dev.storage.ItemTypeStorage;

import java.util.Set;

@Factory
public class ItemTypeStorageConfig {

    @Bean
    public ItemTypeStorage<ItemMapper<?>> itemMappers(Set<ItemMapper<?>> mappers) {
        return new ItemTypeStorage<>(mappers);
    }

    @Bean
    public ItemTypeStorage<ReactiveStreamsCrudRepository<?, Long>> itemRepositories(Set<ReactiveStreamsCrudRepository<?, Long>> repositories) {
        return new ItemTypeStorage<>(repositories);
    }
}
