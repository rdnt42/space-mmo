package marowak.dev.storage;

import marowak.dev.enums.ItemType;
import marowak.dev.service.item.mark.ItemTypeService;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class ItemTypeStorage<T> {
    private final Map<ItemType, T> initServicesMap = new EnumMap<>(ItemType.class);

    public ItemTypeStorage(Set<T> initServices) {
        initServiceMap(initServices);
    }

    private void initServiceMap(Set<T> services) {
        for (T service : services) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for (Class<?> targetInterface : interfaces) {
                if (ItemTypeService.class.isAssignableFrom(targetInterface)) {
                    ItemTypeService itemTypeService = (ItemTypeService) service;
                    throwIfValueExists(itemTypeService.getItemType());
                    initServicesMap.put(itemTypeService.getItemType(), service);
                }
            }
        }
    }


    private void throwIfValueExists(ItemType type) {
        initServicesMap.computeIfPresent(type, (key, value) -> {
            throw new IllegalArgumentException("Value for key " + key + " already exists. Value: " + value);
        });
    }

    public T getService(ItemType type) {
        return initServicesMap.get(type);
    }
}
