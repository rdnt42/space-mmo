package marowak.dev.service.probability;

import reactor.core.publisher.Mono;

public interface ProbabilityCalculationService {
    Mono<Boolean> isItemDropped(int typeId);
}
