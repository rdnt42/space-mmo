package marowak.dev.service.character;

import io.micronaut.http.HttpStatus;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.model.mongo.Character;
import marowak.dev.repository.mongo.CharacterRepository;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:55
 */
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;

    @Override
    public Publisher<Character> getCharacters(String username) {
        return characterRepository.getAll(username);
    }

    @Override
    public Mono<HttpStatus> createCharacter(CharacterRequest request, String username) {
        Character character = new Character(request.characterName(), 0, username);

        return characterRepository.save(character)
                .map(added -> Boolean.TRUE.equals(added) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
