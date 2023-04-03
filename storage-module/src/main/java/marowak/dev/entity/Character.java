package marowak.dev.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.Builder;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@MappedEntity(value = "characters")
public record Character(

        @Id
        String characterName,

        @NotBlank
        String accountName,

        @NotNull
        int experience,

        @NotNull
        int x,
        @NotNull
        int y,

        @NotNull
        int angle,

        @Column(name = "is_online")
        boolean online
        ) {
}
