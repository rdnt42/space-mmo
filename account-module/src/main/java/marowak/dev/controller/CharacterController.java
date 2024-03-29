package marowak.dev.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterRequest;
import marowak.dev.service.character.CharacterService;

import javax.validation.Valid;
import java.security.Principal;

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

    @Post
    public void create(@Valid CharacterRequest request, Principal principal) {
        characterService.createCharacter(request, principal.getName());
    }
}
