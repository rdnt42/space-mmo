package marowak.dev.service;

import marowak.dev.entity.Engine;
import marowak.dev.request.message.EngineRequest;

public interface EngineService {
    Engine create(EngineRequest request);
}
