package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Engine;
import marowak.dev.repository.EngineRepository;
import marowak.dev.request.message.EngineRequest;

@RequiredArgsConstructor
@Singleton
public class EngineServiceImpl implements EngineService {
    private final EngineRepository engineRepository;

    @Override
    public Engine create(EngineRequest request) {
        Engine engine = Engine.builder()
                .characterName(request.characterName())
                .engineTypeId(request.engineTypeId())
                .speed(request.speed())
                .cost(request.cost())
                .build();

        return null;
//        return engineRepository.save(engine);
    }

}
