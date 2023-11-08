package marowak.dev.service.probability;

import jakarta.inject.Singleton;
import marowak.dev.enums.ItemType;
import reactor.core.publisher.Mono;

@Singleton
public class ProbabilityCalculationServiceImpl implements ProbabilityCalculationService {
    @Override
    public Mono<Boolean> isItemDropped(int typeId) {
        if (typeId == ItemType.ITEM_TYPE_HULL.getTypeId()) {
            return Mono.just(false);
        }

        return Mono.just(Math.random() < 0.6);
    }
}
