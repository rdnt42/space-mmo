package marowak.dev.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import marowak.dev.model.mongo.Character;
import marowak.dev.request.CharacterRequest;
import marowak.dev.service.character.CharacterService;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 22:03
 */
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
@Validated
@Controller("/characters")
public class CharacterController {
    private final CharacterService characterService;

    @Get
    public Publisher<Character> getCharacters() {
        return characterService.getCharacters();
    }

    @Post
    public Mono<HttpStatus> create(@Valid CharacterRequest request) {
        return characterService.createCharacter(request);
    }
}
