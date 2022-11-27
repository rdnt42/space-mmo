package marowak.dev.request;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 22.11.2022
 * Time: 23:25
 */
@Introspected
public record AccountRequest(
        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotBlank
        String email) {
}
