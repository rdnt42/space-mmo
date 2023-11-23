package marowak.dev.service.command.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.service.character.CharacterService;
import marowak.dev.service.command.CharacterRequestCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class UpdateMotionCmd implements CharacterRequestCommand<Object, Void> {
    private final CharacterService characterService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> execute(Object request, String characterName) {
        CharacterMotionRequest value = objectMapper.convertValue(request, CharacterMotionRequest.class);

        return characterService.updateCharacterMotion(value, characterName);
    }
}
