package marowak.dev.service.character;

import io.micronaut.http.HttpStatus;
import marowak.dev.model.mongo.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:55
 */
public interface CharacterService {
    Publisher<Character> getCharacters();

    Mono<HttpStatus> createCharacter(CharacterRequest request);
}
