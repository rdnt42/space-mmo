package marowak.dev.service.command.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.service.bullet.BulletMotionService;
import marowak.dev.service.command.CharacterRequestCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class UpdateShootingCmd implements CharacterRequestCommand<Object, Void> {
    private final BulletMotionService bulletMotionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> execute(Object request, String characterName) {
        CharacterShootingRequest value = objectMapper.convertValue(request, CharacterShootingRequest.class);

        return bulletMotionService.updateShooting(value, characterName);
    }
}
