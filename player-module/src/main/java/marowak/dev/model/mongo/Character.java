package marowak.dev.model.mongo;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:43
 */
@Getter
@Serdeable
public record Character(
        @NotBlank
        String name,

        @NonNull
        Integer experience,

        @NotBlank
        String accountName
) {
    @Creator
    @BsonCreator
    public Character(@NotBlank String name, @NonNull Integer experience, @NotBlank String accountName) {
        this.name = name;
        this.experience = experience;
        this.accountName = accountName;
    }
}
