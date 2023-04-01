package marowak.dev.service;

import marowak.dev.entity.Engine;
import marowak.dev.request.message.EngineRequest;
import org.reactivestreams.Publisher;

public interface EngineService {
    Publisher<Engine> create(EngineRequest request);
}
