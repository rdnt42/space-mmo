package marowak.dev.repository.mongo;

import io.micronaut.core.annotation.NonNull;
import marowak.dev.model.mongo.Character;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:53
 */
public interface CharacterRepository {
    Publisher<Character> getAll(String username);

    Mono<Boolean> save(@NonNull @Valid Character character);
}
