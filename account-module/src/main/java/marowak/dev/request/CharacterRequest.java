package marowak.dev.request;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:59
 */
@Introspected
public record CharacterRequest(
        @NotBlank
        String characterName
) {
}
