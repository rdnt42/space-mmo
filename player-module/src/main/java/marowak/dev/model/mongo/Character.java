package marowak.dev.model.mongo;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:43
 */
@Serdeable
public record Character(
        @NotBlank
        String name,

        @NonNull
        Integer experience,

        @NotBlank
        String accountName
) {
}
